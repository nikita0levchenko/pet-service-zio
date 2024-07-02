package chapter4.exercises

import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise6 extends ZIOAppDefault {

  /*
  Using the ZIO#refineToOrDie method, narrow the error type of the fol- lowing effect to just NumberFormatException.
   */

  val parseNumber: ZIO[Any, Throwable, Int] =
    ZIO.attempt("foo".toInt).refineToOrDie[NumberFormatException]

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- parseNumber
  } yield ()
}
