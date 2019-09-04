package nl.buurtbuik

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import cats.implicits._
import nl.buurtbuik.repository.PermissionRepository
import org.http4s.dsl.io._
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedRoutes, BasicCredentials, Request}

case class User(user: String)

object Authentication {

  private def retrieveUser: Kleisli[IO, BasicCredentials, User] = Kleisli(credentials => IO({
    User(credentials.username)
  }))

  def validate(header: String): Option[BasicCredentials] = {
    val payload = header.split(" ").lastOption
    if (payload.isDefined) {
      val credentials = BasicCredentials(payload.get)
      val permission = PermissionRepository.getPermission(credentials.username, credentials.password)
      if (permission.isDefined) {
        return Option(credentials)
      }
    }
      Option.empty
  }

  private val authUser: Kleisli[IO, Request[IO], Either[String, User]] = Kleisli({ request =>
    val credentials = for {
      header <- request.headers.get(Authorization).toRight("Couldn't find an Authorization header")
      credentials <- validate(header.value).toRight("Authorization header validation failed")
    } yield credentials
    credentials.traverse(retrieveUser.run)
  })

  private val onFailure: AuthedRoutes[String, IO] = Kleisli(req => OptionT.liftF(Forbidden(req.authInfo)))

  val middleware: AuthMiddleware[IO, User] =
    AuthMiddleware(authUser, onFailure)

}
