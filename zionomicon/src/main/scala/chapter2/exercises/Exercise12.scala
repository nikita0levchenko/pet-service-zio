package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault
import zio.Console

object Exercise12 extends ZIOAppDefault {

  /*
  Using ZIO.fail and ZIO.succeed, implement the following function, which converts a List into a ZIO effect,
  by looking at the head element in the list and ignoring the rest of the elements.
   */

  def listToZIO[A](list: List[A]): ZIO[Any, None.type, A] = list match {
    case Nil    => ZIO.fail(None)
    case x :: _ => ZIO.succeed(x)
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    goodResult <- listToZIO(List(1, 2, 3, 4))
    _ <- Console.printLine(goodResult)
    badResult <- listToZIO(List.empty)
    _ <- Console.printLine(badResult)
  } yield ()
}
