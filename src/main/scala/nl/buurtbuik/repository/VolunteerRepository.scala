package nl.buurtbuik.repository

import cats.effect.IO
import doobie.implicits._
import io.circe.generic.JsonCodec
import nl.buurtbuik.Application.xa
import nl.buurtbuik.endpoint.{VolunteerPostData, VolunteerPutData}

object VolunteerRepository {

  @JsonCodec
  case class Volunteer(id: Int,
                       email: String,
                       password: String,
                       firstName: String,
                       lastName: String,
                       phone: String,
                       admin: Boolean)

  def getById(id: Int): Option[Volunteer] =
    sql"select * from buurtbuik.volunteers where id = $id"
      .query[Volunteer].option.transact(xa).unsafeRunSync()

  def getByEmail(email: String): Option[Volunteer] =
    sql"select * from buurtbuik.volunteers where email = $email"
      .query[Volunteer].option.transact(xa).unsafeRunSync()

  def getAll: List[Volunteer] =
    sql"select * from buurtbuik.volunteers".query[Volunteer].to[List].transact(xa).unsafeRunSync()

  def insert(v: VolunteerPostData): IO[Int] =
    sql"insert into buurtbuik.volunteers values (default, ${v.email}, ${v.password}, ${v.firstName}, ${v.lastName}, ${v.phone}, false)"
      .update.run.transact(xa)

  def update(v: VolunteerPutData, id: Int): IO[Int] =
    sql"update buurtbuik.volunteers set email=${v.email}, first_name=${v.firstName}, last_name=${v.lastName}, phone=${v.phone} where buurtbuik.volunteers.id = $id"
      .update.run.transact(xa)

}
