package nl.buurtbuik.repository

import doobie.implicits._
import io.circe.generic.JsonCodec
import nl.buurtbuik.Application

object VolunteerRepository {

  @JsonCodec
  case class Volunteer(id: Int, email: String, firstName: String, lastName: String, phone: String)

  def getById(id: Int): Option[Volunteer] =
    sql"select * from buurtbuik.volunteers where id = $id".query[Volunteer].option.transact(Application.xa).unsafeRunSync()

  def getAll: List[Volunteer] =
    sql"select * from buurtbuik.volunteers".query[Volunteer].to[List].transact(Application.xa).unsafeRunSync()
}
