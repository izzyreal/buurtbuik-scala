package nl.buurtbuik.endpoint

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

object HelloEndpoints {
  val helloEndpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")
  }
}
