package chapter2.exercises

import zio.ZIO
import zio.Console
import zio.Scope
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise11 extends ZIOAppDefault {
  /*
  Using ZIO.fail and ZIO.succeed, implement the following function, which converts an Either into a ZIO effect:
   */

  def eitherToZIO[E, A](either: Either[E, A]): ZIO[Any, E, A] =
    either match {
      case Right(value) => ZIO.succeed(value)
      case Left(error)  => ZIO.fail(error)
    }

  val ex11check: ZIO[Any, Exception, Unit] = for {
    goodRes <- eitherToZIO(Right(1))
    _ <- Console.printLine(goodRes)
    badRes <- eitherToZIO(Left(new RuntimeException("something bad")))
    _ <- Console.printLine(badRes)
  } yield ()

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- ex11check
  } yield ()
}
