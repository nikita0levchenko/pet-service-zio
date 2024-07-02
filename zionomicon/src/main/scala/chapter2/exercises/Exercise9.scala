package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault
import zio.Console

object Exercise9 extends ZIOAppDefault {
  /*
  Implement the orElse function in terms of the toy model of a ZIO effect.
  The function should return an effect that tries the left hand side, but if that effect fails,
  it will fallback to the effect on the right hand side.
   */

  def orElse[R, E1, E2, A](
      self: ToyZIO[R, E1, A],
      that: ToyZIO[R, E2, A]
  ): ToyZIO[R, E2, A] =
    ToyZIO { r =>
      self.run(r) match {
        case Left(e1) => that.run(r)
        case Right(a) => Right(a)
      }
    }

  val problemEffect: ToyZIO[Any, Int, Int] = ToyZIO(_ => Left(0))
  val goodEffect: ToyZIO[Any, Int, Int] = ToyZIO(_ => Right(1))
  val orElseEffect: ToyZIO[Any, Int, Int] =
    orElse(problemEffect, goodEffect)

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- orElseEffect.run() match {
      case Right(value) => Console.printLine(value)
      case Left(error)  => Console.printLine(error)
    }
  } yield ()
}
