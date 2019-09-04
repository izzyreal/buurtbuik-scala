package nl.buurtbuik.repository

import java.sql.Timestamp
import java.time.{LocalDateTime, ZonedDateTime}

import doobie.implicits._
import io.circe.generic.JsonCodec
import nl.buurtbuik.Application

object EventRepository {

  @JsonCodec
  case class Event(id: Int, startAt: String, endAt: String)

  def getEventById(id: Int): Option[Event] =
    sql"select * from buurtbuik.events where id = $id".query[Event].option.transact(Application.xa).unsafeRunSync()

  def getEvents: List[Event] =
    sql"select * from buurtbuik.events"
      .query[(Int, Timestamp, Timestamp)]
      .map(e => Event(0, e._2.toLocalDateTime.toString, e._3.toLocalDateTime.toString))
      .to[List]
      .transact(Application.xa).unsafeRunSync()

}
