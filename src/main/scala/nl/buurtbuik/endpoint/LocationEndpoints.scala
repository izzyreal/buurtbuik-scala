package nl.buurtbuik.endpoint

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import nl.buurtbuik.repository.LocationRepository
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._

object LocationEndpoints {

  val locationEndpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case GET -> Root / "locations" =>
      Ok(LocationRepository.getLocations.asJson)

    case GET -> Root / "locations" / IntVar(id) =>
      val location = LocationRepository.getLocation(id)
      if (location.isDefined) {
        Ok(location.asJson)
      } else {
        NotFound(s"Location $id not found!")
      }

  }
}
