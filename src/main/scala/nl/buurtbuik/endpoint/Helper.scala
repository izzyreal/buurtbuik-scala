package nl.buurtbuik.endpoint

import cats.effect.IO
import io.circe.Encoder
import io.circe.syntax._
import org.http4s.Response
import org.http4s.circe._
import org.http4s.dsl.io._

import scala.reflect.runtime.universe.{typeOf, TypeTag}

object Helper {

  private def typeName[T: TypeTag] = typeOf[T].typeSymbol.name.toString

  def getById[A: Encoder : TypeTag](id: Int, supplier: Int => Option[A]): IO[Response[IO]] = {
    val result = supplier(id)
    if (result.isDefined)
      Ok(result.asJson)
    else {
      NotFound(s"${typeName[A]} $id not found!")
    }
  }
}
