package nl.buurtbuik

import cats.effect._
import cats.implicits._
import doobie.Transactor
import doobie.util.transactor.Transactor.Aux
import nl.buurtbuik.endpoint.LocationEndpoints
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._
import org.http4s.server.middleware.{CORS, CORSConfig}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object Application extends IOApp {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", "jdbc:postgresql://localhost:5432/postgres", "postgres", "docker"
  )

  val corsConfig = CORSConfig(
    anyOrigin = true,
    anyMethod = false,
    allowedOrigins = Set("foo.com", "bar.com"),
    allowedMethods = Some(Set("GET", "POST")),
    allowCredentials = true,
    maxAge = 1.day.toSeconds)

  private val services = CORS(Authentication.middleware(LocationEndpoints.locationEndpoints), corsConfig)
  private val httpApp = Router("/" -> services).orNotFound

  def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO].bindHttp(8080, "localhost").withHttpApp(httpApp)
      .serve.compile.drain.as(ExitCode.Success)
  }
}
