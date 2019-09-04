package nl.buurtbuik.repository

import doobie.implicits._
import io.circe.generic.JsonCodec
import nl.buurtbuik.Application

object LocationRepository {

  @JsonCodec
  case class Location(id: Int, name: String)

  def getById(id: Int): Option[Location] =
    sql"select * from buurtbuik.locations where id = $id".query[Location].option.transact(Application.xa).unsafeRunSync()

  def getAll: List[Location] =
    sql"select * from buurtbuik.locations".query[Location].to[List].transact(Application.xa).unsafeRunSync()
}
