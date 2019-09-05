package nl.buurtbuik.repository

import java.sql.Timestamp

import doobie.implicits._
import io.circe.generic.JsonCodec
import nl.buurtbuik.Application

object EventRepository {

  @JsonCodec
  case class Event(id: Int, startAt: String, endAt: String)

  def getById(id: Int): Option[Event] =
    sql"select * from buurtbuik.events where id = $id"
      .query[(Int, Timestamp, Timestamp)]
      .map(tupleToEvent)
      .option
      .transact(Application.xa).unsafeRunSync()

  def getAll: List[Event] =
    sql"select * from buurtbuik.events"
      .query[(Int, Timestamp, Timestamp)]
      .map(tupleToEvent)
      .to[List]
      .transact(Application.xa).unsafeRunSync()

  private def tupleToEvent(t: (Int, Timestamp, Timestamp)): Event = {
    Event(t._1, t._2.toLocalDateTime.toString, t._3.toLocalDateTime.toString)
  }
}
