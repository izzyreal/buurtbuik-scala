package nl.buurtbuik.repository

import doobie.implicits._
import io.circe.generic.JsonCodec
import nl.buurtbuik.Application

object VolunteerRepository {

  @JsonCodec
  case class Volunteer(id: Int,
                       email: String,
                       rolesId: Int,
                       firstName: String,
                       lastName: String,
                       phone: String,
                       defaultLocationId: Int)

  def getById(id: Int): Option[Volunteer] =
    sql"select * from buurtbuik.volunteers where id = $id"
      .query[Volunteer].option.transact(Application.xa).unsafeRunSync()

  def getAll: List[Volunteer] =
    sql"select * from buurtbuik.volunteers".query[Volunteer].to[List].transact(Application.xa).unsafeRunSync()

  def insert(v: Volunteer) =
    sql"insert into buurtbuik.volunteers values (default, ${v.email}, 1, ${v.firstName}, ${v.lastName}, ${v.phone}, 1)"
      .update.run.transact(Application.xa)

}
