package nl.buurtbuik.endpoint

import cats.effect.IO
import nl.buurtbuik.repository.LocationRepository
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

object LocationEndpoints {

  private val locations = LocationRepository.getLocations.map( l => l.id + " " + l.name ).mkString(", ")

  val locationEndpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case GET -> Root / "locations" =>
      Ok(s"Hello $locations")
  }
}
