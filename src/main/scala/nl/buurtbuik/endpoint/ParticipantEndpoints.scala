package nl.buurtbuik.endpoint

import cats.effect.IO
import io.circe.syntax._
import nl.buurtbuik.User
import nl.buurtbuik.repository.ParticipantRepository
import nl.buurtbuik.repository.ParticipantRepository.Participant
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{AuthedRoutes, EntityDecoder, EntityEncoder}

object ParticipantEndpoints {

  private implicit val decoder: EntityDecoder[IO, Participant] = jsonOf[IO, Participant]
  private implicit val intEncoder: EntityEncoder[IO, Int] = jsonEncoderOf[IO, Int]

  private val path = Root / "participants"

  val participantEndpoints: AuthedRoutes[User, IO] = AuthedRoutes.of {

    case GET -> path / IntVar(eventId) as user =>
      Ok(ParticipantRepository.getByEventId(eventId).asJson)

    case authReq@POST -> Root / "participants" as user =>
      authReq.req.as[Participant].flatMap(ParticipantRepository.insert).flatMap(Created(_))

  }
}
