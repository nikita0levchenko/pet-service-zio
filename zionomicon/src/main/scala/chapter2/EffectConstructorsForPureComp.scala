package chapter2

import zio.IO
import zio.Scope
import zio.Task
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

import scala.util.Try

object EffectConstructorsForPureComp extends ZIOAppDefault{

  /*
  Use this constructors for pure computations
   */

  val zioSuccess: ZIO[Any, Nothing, Int] = ZIO.succeed(21 + 21)

  val zioFailure: ZIO[Any, NullPointerException, Nothing] = ZIO.fail(new NullPointerException("Pure npe from zio"))

  /*
  Since ZIO.attempt can't work with Option, Either and e.t.c. monads - ZIO have few methods to construct
  effect from this monads
   */

  val zioEither: IO[Nothing, String] = ZIO.fromEither(Right("Right 42"))

  val zioTry: Task[String] = ZIO.fromTry(Try("some success from Try"))

  val zioOption: IO[Option[Nothing], Int] = ZIO.fromOption(Option(21 + 22))

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- zioSuccess.map(println)
    _ <- zioEither.map(println)
    _ <- zioTry.map(println)
    _ <- zioOption.map(println)
    _ <- zioFailure
  } yield ()
}
