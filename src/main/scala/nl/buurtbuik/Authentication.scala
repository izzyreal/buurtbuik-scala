package nl.buurtbuik

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import cats.implicits._
import org.http4s.dsl.io._
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedRoutes, BasicCredentials, Request}

case class User(user: String, pass: String)

object Authentication {

  private def retrieveUser: Kleisli[IO, BasicCredentials, User] = Kleisli(credentials => IO({
    User(credentials.username, credentials.password)
  }))

  def validateBasicAuthHeader(header: String): Option[BasicCredentials] = {
    val payload = header.split(" ").lastOption
    if (payload.isDefined) {
      Option(BasicCredentials(payload.get))
    } else {
      Option.empty
    }
  }

  private val authUser: Kleisli[IO, Request[IO], Either[String, User]] = Kleisli({ request =>
    val credentials = for {
      header <- request.headers.get(Authorization).toRight("Couldn't find an Authorization header")
      credentials <- validateBasicAuthHeader(header.value).toRight("Invalid token")
    } yield credentials
    credentials.traverse(retrieveUser.run)
  })

  private val onFailure: AuthedRoutes[String, IO] = Kleisli(req => OptionT.liftF(Forbidden(req.authInfo)))

  val middleware: AuthMiddleware[IO, User] =
    AuthMiddleware(authUser, onFailure)

}
