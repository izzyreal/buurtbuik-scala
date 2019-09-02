package nl.buurtbuik.endpoint

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

object TweetEndpoints {
  val tweetEndpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "tweets" / name =>
      Ok(s"Tweets, $name.")
  }
}
