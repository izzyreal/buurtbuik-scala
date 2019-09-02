package nl.buurtbuik.repository

import doobie.implicits._
import nl.buurtbuik.Application

object LocationRepository {

  case class Location(id: Int, name: String)

  def getLocations: List[Location] =
    sql"select * from buurtbuik.locations".query[Location].to[List].transact(Application.xa).unsafeRunSync()
}
