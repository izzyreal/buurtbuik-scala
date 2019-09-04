package nl.buurtbuik.repository

import doobie.implicits._
import io.circe.generic.JsonCodec
import nl.buurtbuik.Application

object PermissionRepository {

  @JsonCodec
  case class Permission(id: Int, email: String, password: String, admin: Boolean)

  def getByEmailAndPassword(email: String, password: String): Option[Permission] =
    sql"select * from buurtbuik.permissions where email = $email and password = $password".query[Permission].option.transact(Application.xa).unsafeRunSync()

}
