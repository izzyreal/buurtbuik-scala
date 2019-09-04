package nl.buurtbuik.endpoint

import cats.effect.IO
import io.circe.syntax._
import nl.buurtbuik.User
import nl.buurtbuik.repository.VolunteerRepository
import nl.buurtbuik.repository.VolunteerRepository.Volunteer
import org.http4s.{AuthedRoutes, EntityDecoder, EntityEncoder, HttpRoutes, Request}
import org.http4s.circe._
import org.http4s.dsl.io._

object VolunteerEndpoints {

  private implicit val decoder: EntityDecoder[IO, Volunteer] = jsonOf[IO, Volunteer]
  private implicit val encoder: EntityEncoder[IO, Int] = jsonEncoderOf[IO, Int]

  val volunteerEndpoints: AuthedRoutes[User, IO] = AuthedRoutes.of {

    case GET -> Root / "volunteers" as _ =>
      Ok(VolunteerRepository.getAll.asJson)

    case GET -> Root / "volunteers" / IntVar(id) as _ =>
      Helper.getById(id, VolunteerRepository.getById)

    case authReq@POST -> Root / "volunteers" as user =>
        authReq.req.as[Volunteer].flatMap(VolunteerRepository.insert).flatMap(Created(_))
  }

}
