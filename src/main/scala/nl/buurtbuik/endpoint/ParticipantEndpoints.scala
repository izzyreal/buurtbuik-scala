package nl.buurtbuik.endpoint

import cats.effect.IO
import io.circe.generic.JsonCodec
import io.circe.syntax._
import nl.buurtbuik.Application.intEncoder
import nl.buurtbuik.User
import nl.buurtbuik.repository.ParticipantRepository.Participant
import nl.buurtbuik.repository.{ParticipantRepository, VolunteerRepository}
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{AuthedRoutes, EntityDecoder, EntityEncoder}

object ParticipantEndpoints {

  @JsonCodec
  case class IsCurrentUserParticipatingResponse(result: Boolean, currentUserId: Int)

  private implicit val decoder: EntityDecoder[IO, Participant] = jsonOf[IO, Participant]
  private implicit val encoder: EntityEncoder[IO, IsCurrentUserParticipatingResponse] = jsonEncoderOf[IO, IsCurrentUserParticipatingResponse]

  val participantEndpoints: AuthedRoutes[User, IO] = AuthedRoutes.of {

    case GET -> Root / "participants" / IntVar(eventId) as user =>
      Ok(ParticipantRepository.getByEventId(eventId).asJson)

    case authReq@POST -> Root / "participants" as user =>
      authReq.req.as[Participant].flatMap(ParticipantRepository.insert).flatMap(Created(_))

    case GET -> Root / "participants" / "is-current-user-participating" / IntVar(eventId) as user =>
      val volunteer = VolunteerRepository.getByEmail(user.user).get
      val p = ParticipantRepository.getByEventId(eventId).find(p => p.volunteerId == volunteer.id)
      Ok(IsCurrentUserParticipatingResponse(p.isDefined, volunteer.id))
  }
}
