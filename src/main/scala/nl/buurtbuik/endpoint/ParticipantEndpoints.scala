package nl.buurtbuik.endpoint

import cats.effect.IO
import io.circe.generic.JsonCodec
import io.circe.syntax._
import nl.buurtbuik.Application.intEncoder
import nl.buurtbuik.User
import nl.buurtbuik.repository.{ParticipantRepository, VolunteerRepository}
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{AuthedRoutes, EntityDecoder, EntityEncoder}

object ParticipantEndpoints {

  @JsonCodec
  case class IsCurrentUserParticipatingResponse(result: Boolean, currentUserId: Int)

  @JsonCodec
  case class ParticipantPostData(volunteerId: Int, eventId: Int)

  private implicit val decoder: EntityDecoder[IO, ParticipantPostData] = jsonOf[IO, ParticipantPostData]
  private implicit val encoder: EntityEncoder[IO, IsCurrentUserParticipatingResponse] = jsonEncoderOf[IO, IsCurrentUserParticipatingResponse]

  val participantEndpoints: AuthedRoutes[User, IO] = AuthedRoutes.of {

    case GET -> Root / "participants" / IntVar(eventId) as _ =>
      Ok(ParticipantRepository.getByEventId(eventId).asJson)

    case authReq@POST -> Root / "participants" as _ =>
      authReq.req.as[ParticipantPostData].flatMap(ParticipantRepository.insert).flatMap(Created(_))

    case GET -> Root / "participants" / "is-current-user-participating" / IntVar(eventId) as user =>
      val volunteer = VolunteerRepository.getByEmail(user.email).get
      val p = ParticipantRepository.getByEventId(eventId).find(p => p.volunteerId == volunteer.id)
      Ok(IsCurrentUserParticipatingResponse(p.isDefined, volunteer.id))

    case POST -> Root / "participants" / "current-user" / IntVar(eventId) as user =>
      val volunteerId = VolunteerRepository.getByEmail(user.email).get.id
      ParticipantRepository.insert(ParticipantPostData(volunteerId, eventId)).flatMap(Created(_))

    case DELETE -> Root / "participants" / "current-user" / IntVar(eventId) as user =>
      val volunteerId = VolunteerRepository.getByEmail(user.email).get.id
      Ok(ParticipantRepository.deleteByEventAndVolunteerIds(volunteerId, eventId))

    case DELETE -> Root / "participants" / IntVar(eventId) as _ =>
      Ok(ParticipantRepository.deleteByEventId(eventId))

  }
}
