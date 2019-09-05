package nl.buurtbuik.endpoint

import cats.effect.IO
import io.circe.syntax._
import nl.buurtbuik.User
import nl.buurtbuik.repository.VolunteerRepository
import nl.buurtbuik.repository.VolunteerRepository.Volunteer
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{AuthedRoutes, EntityDecoder}

import scala.collection.immutable.HashMap
import nl.buurtbuik.Application.hashMapEncoder
import nl.buurtbuik.Application.intEncoder

object VolunteerEndpoints {

  private implicit val decoder: EntityDecoder[IO, Volunteer] = jsonOf[IO, Volunteer]

  val volunteerEndpoints: AuthedRoutes[User, IO] = AuthedRoutes.of {

    case GET -> Root / "volunteers" as _ =>
      Ok(VolunteerRepository.getAll.asJson)

    case GET -> Root / "volunteers" / IntVar(id) as _ =>
      Helper.getById(id, VolunteerRepository.getById)

    case authReq@POST -> Root / "volunteers" as user =>
      authReq.req.as[Volunteer].flatMap(VolunteerRepository.insert).flatMap(Created(_))

    case GET -> Root / "volunteers" / "is-current-user-admin" as user =>
      val v = VolunteerRepository.getByEmail(user.user)
      if (v.isDefined) {
        Ok(HashMap("admin" -> v.get.admin))
      } else {
        Ok(HashMap("admin" -> false))
      }

    case authReq@PUT -> Root / "volunteers" / IntVar(id) as user =>
      authReq.req.as[Volunteer].flatMap(VolunteerRepository.update).flatMap(_ => NoContent())

  }
}
