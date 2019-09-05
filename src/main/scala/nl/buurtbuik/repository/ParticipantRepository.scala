package nl.buurtbuik.repository

import cats.effect.IO
import doobie.implicits._
import io.circe.generic.JsonCodec
import nl.buurtbuik.Application

object ParticipantRepository {

  @JsonCodec
  case class Participant(eventId: Int, volunteerId: Int)

  def insert(p: Participant): IO[Int] =
    sql"insert into buurtbuik.participants values (${p.volunteerId}, ${p.eventId})"
      .update.run.transact(Application.xa)

  def getAll: List[Participant] =
    List.empty

  def getByEventId(eventId: Int): List[Participant] =
    List.empty

  def deleteByEventId(eventId: Int): Unit = {}

  def deleteByEventAndVolunteerIds(volunteerId: Int, eventId: Int): Unit = {}

}
