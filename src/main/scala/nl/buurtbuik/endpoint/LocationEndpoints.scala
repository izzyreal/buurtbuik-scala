package nl.buurtbuik.endpoint

import cats.effect.IO
import io.circe.syntax._
import nl.buurtbuik.User
import nl.buurtbuik.repository.LocationRepository
import org.http4s.AuthedRoutes
import org.http4s.circe._
import org.http4s.dsl.io._

object LocationEndpoints {

  val locationEndpoints: AuthedRoutes[User, IO] = AuthedRoutes.of {

    case GET -> Root / "locations" as _ =>
      Ok(LocationRepository.getAll.asJson)

    case GET -> Root / "locations" / IntVar(id) as _ =>
      Helper.getById(id, LocationRepository.getById)

  }

}
