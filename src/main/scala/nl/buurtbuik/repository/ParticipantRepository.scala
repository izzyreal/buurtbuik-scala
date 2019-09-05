package nl.buurtbuik.repository

import cats.effect.IO
import doobie.implicits._
import io.circe.generic.JsonCodec
import nl.buurtbuik.Application.xa
import nl.buurtbuik.endpoint.ParticipantEndpoints.ParticipantPostData

object ParticipantRepository {

  @JsonCodec
  case class ParticipantResponse(volunteerId: Int, volunteerName: String, eventId: Int)

  def insert(p: ParticipantPostData): IO[Int] =
    sql"insert into buurtbuik.participants values (${p.volunteerId}, ${p.eventId})"
      .update.run.transact(xa)

  def getAll: List[ParticipantResponse] =
    sql"select * from buurtbuik.participants"
      .query[ParticipantResponse].to[List].transact(xa).unsafeRunSync()

  def getByEventId(eventId: Int): List[ParticipantResponse] =
    sql"select buurtbuik.participants.volunteer_id, buurtbuik.participants.event_id, buurtbuik.volunteers.first_name, buurtbuik.volunteers.last_name from buurtbuik.participants inner join buurtbuik.volunteers on buurtbuik.volunteers.id = buurtbuik.participants.volunteer_id where event_id = $eventId"
      .query[(Int, Int, String, String)].map(tupleToParticipantResponse).to[List].transact(xa).unsafeRunSync()

  def deleteByEventId(eventId: Int): Unit =
    sql"delete from buurtbuik.participants where event_id = $eventId"
      .update.run.transact(xa).unsafeRunSync()

  def deleteByEventAndVolunteerIds(volunteerId: Int, eventId: Int): Unit =
    sql"delete from buurtbuik.participants where event_id = $eventId and volunteer_id = $volunteerId"
      .update.run.transact(xa).unsafeRunSync()

  private def tupleToParticipantResponse(t: (Int, Int, String, String)) =
    ParticipantResponse(t._1, t._3 + " " + t._4, t._2)
}
