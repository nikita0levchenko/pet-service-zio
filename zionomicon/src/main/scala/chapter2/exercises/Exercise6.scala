package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault
import zio.Console

object Exercise6 extends ZIOAppDefault {
  /*
  6. Implement the zipWith function in terms of the toy model of a ZIO effect.
  The function should return an effect that sequentially composes the specified effects,
  merging their results with the specified user-defined function.
   */

  def zipWith[R, E, A, B, C](self: ToyZIO[R, E, A], that: ToyZIO[R, E, B])(
      f: (A, B) => C
  ): ToyZIO[R, E, C] =
    ToyZIO(r =>
      for {
        selfRes <- self.run(r)
        thatRes <- that.run(r)
        effectC = f(selfRes, thatRes)
      } yield effectC
    )

  val selfEffect: ToyZIO[Any, Throwable, Int] =
    ToyZIO[Any, Throwable, Int](_ => Right(3 * 2))
  val thatEffect: ToyZIO[Any, Throwable, Char] =
    ToyZIO[Any, Throwable, Char](_ => Right('x'))
  val combinatorF: (Int, Char) => String = (i, c) =>
    s"This is my currency: ($i, $c)"
  val zippedZioEx6 = zipWith(selfEffect, thatEffect)(combinatorF)

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- zippedZioEx6.run() match {
      case Right(value) => Console.printLine(value)
      case Left(error)  => Console.printLine(error.toString)
    }
  } yield ()
}
