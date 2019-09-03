package nl.buurtbuik.repository

import doobie.implicits._
import nl.buurtbuik.Application

object LocationRepository {

  case class Location(id: Int, name: String)

  def getLocation(id: Int): Option[Location] =
    sql"select * from buurtbuik.locations where id = $id".query[Location].option.transact(Application.xa).unsafeRunSync()

  def getLocations: List[Location] =
    sql"select * from buurtbuik.locations".query[Location].to[List].transact(Application.xa).unsafeRunSync()
}
