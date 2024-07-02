package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.Console
import zio.Random
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise18 extends ZIOAppDefault {

  /*
  Using the Console and Random services in ZIO, write a little program that asks the user to guess a randomly chosen number between 1 and 3,
  and prints out if they were correct or not.
   */

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    randomInt <- Random.nextIntBetween(1, 3)
    _ <- Console.printLine(
      "Try to guess integer between 1 and 3, borders included:"
    )
    answer <- Console.readLine
    _ <-
      if (randomInt.toString == answer) Console.printLine("Right answer!")
      else Console.printLine(s"Bad answer... The right answer was: $randomInt")
  } yield ()
}
