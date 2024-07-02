package chapter2.exercises

import chapter2.exercises.Exercise7.collectAll
import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault
import zio.Console

object Exercise8 extends ZIOAppDefault {
  /*
  8. Implement the foreach function in terms of the toy model of a ZIO effect.
  The function should return an effect that sequentially runs the specified function on every element of the specified collection.
   */

  def foreach[R, E, A, B](in: Iterable[A])(
      f: A => ToyZIO[R, E, B]
  ): ToyZIO[R, E, List[B]] = collectAll(in.map(f))

  val foreachedZIO: ToyZIO[Any, Nothing, List[Int]] =
    foreach(List(1, 2, 3))(elem =>
      ToyZIO(_ => {
        println(s"This is elem: $elem")
        Right(elem + 1)
      })
    )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- foreachedZIO.run() match {
      case Right(list) => Console.printLine(list)
      case Left(error) => Console.printLine(error)
    }
  } yield ()
}
