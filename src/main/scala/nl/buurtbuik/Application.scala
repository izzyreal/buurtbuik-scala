package nl.buurtbuik

import cats.effect._
import cats.implicits._
import nl.buurtbuik.endpoint.{HelloEndpoints, TweetEndpoints}
import org.http4s.server.Router
import org.http4s.server.blaze._
import org.http4s.implicits._

object Application extends IOApp {

  private val services =  TweetEndpoints.tweetEndpoints <+> HelloEndpoints.helloEndpoints
  private val httpApp = Router("/" -> services).orNotFound

  def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO].bindHttp(8080, "localhost").withHttpApp(httpApp)
      .serve.compile.drain.as(ExitCode.Success)
  }
}
