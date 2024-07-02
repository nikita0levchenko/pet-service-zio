package chapter2.exercises

import zio.Scope
import zio.Task
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise20 extends ZIOAppDefault {

  /*
  Using recursion, write a function that will continue evaluating the specified effect,
  until the specified user-defined function evaluates to true on the output of the effect.
   */

  var count = 0

  val effect: Task[Int] = ZIO.attempt {
    println(s"Counted: $count")
    count += 1
    count
  }

  def doWhile[R, E, A](body: ZIO[R, E, A])(
      condition: A => Boolean
  ): ZIO[R, E, A] = body.flatMap { effect =>
    if (condition(effect)) {
      println(s"Final count: ${effect.toString}")
      ZIO.succeed(effect)
    } else doWhile(body)(condition)
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- doWhile(effect)(_ == 3)
  } yield ()
}
