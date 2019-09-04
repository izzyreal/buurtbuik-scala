package nl.buurtbuik.endpoint

import cats.effect.IO
import io.circe.syntax._
import nl.buurtbuik.User
import nl.buurtbuik.repository.EventRepository
import org.http4s.AuthedRoutes
import org.http4s.circe._
import org.http4s.dsl.io._

object EventEndpoints {

  val eventEndpoints: AuthedRoutes[User, IO] = AuthedRoutes.of {

    case GET -> Root / "events" as _ =>
      Ok(EventRepository.getEvents.asJson)

    case GET -> Root / "events" / IntVar(id) as _ =>
      Helper.getById(id, EventRepository.getEventById)

  }

}
