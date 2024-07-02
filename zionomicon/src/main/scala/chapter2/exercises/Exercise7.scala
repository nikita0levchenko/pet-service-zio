package chapter2.exercises

import chapter2.exercises.Exercise6.selfEffect
import chapter2.exercises.Exercise6.zipWith
import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault
import zio.Console

object Exercise7 extends ZIOAppDefault {
  /*
  Implement the collectAll function in terms of the toy model of a ZIO effect.
  The function should return an effect that sequentially collects the results of the specified collection of effects.
   */

  def collectAll[R, E, A](
      in: Iterable[ToyZIO[R, E, A]]
  ): ToyZIO[R, E, List[A]] = if (in.isEmpty) ToyZIO(_ => Right(List.empty))
  else zipWith(in.head, collectAll(in.tail))(_ :: _)

  val collectedEffects: ToyZIO[Any, Throwable, List[Int]] = collectAll(
    Seq(selfEffect, selfEffect)
  )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- collectedEffects.run() match {
      case Right(list) => Console.printLine(list)
      case Left(error) => Console.printLine(error.toString)
    }
  } yield ()
}
