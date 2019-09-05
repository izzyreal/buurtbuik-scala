package nl.buurtbuik.repository

import cats.effect.IO
import doobie.implicits._
import io.circe.generic.JsonCodec
import nl.buurtbuik.Application.xa

object ParticipantRepository {

  @JsonCodec
  case class Participant(volunteerId: Int, volunteerName: String, eventId: Int)

  def insert(p: Participant): IO[Int] =
    sql"insert into buurtbuik.participants values (${p.volunteerId}, ${p.eventId})"
      .update.run.transact(xa)

  def getAll: List[Participant] =
    sql"select * from buurtbuik.participants"
      .query[Participant].to[List].transact(xa).unsafeRunSync()

  def getByEventId(eventId: Int): List[Participant] =
    sql"select buurtbuik.participants.volunteer_id, buurtbuik.participants.event_id, buurtbuik.volunteers.first_name, buurtbuik.volunteers.last_name from buurtbuik.participants inner join buurtbuik.volunteers on buurtbuik.volunteers.id = buurtbuik.participants.volunteer_id where event_id = $eventId"
      .query[(Int, Int, String, String)].map(tupleToParticipant).to[List].transact(xa).unsafeRunSync()

  def deleteByEventId(eventId: Int): Unit =
    sql"delete from buurtbuik.participants where event_id = $eventId"
      .update.run.transact(xa).unsafeRunSync()

  def deleteByEventAndVolunteerIds(volunteerId: Int, eventId: Int): Unit =
    sql"delete from buurtbuik.participants where event_id = $eventId and volunteer_id = $volunteerId"
      .update.run.transact(xa).unsafeRunSync()

  private def tupleToParticipant(t: (Int, Int, String, String)) =
    Participant(t._1, t._3 + " " + t._4, t._2)
}
