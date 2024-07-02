package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise5 extends ZIOAppDefault {

  /*
  5. Rewrite the following ZIO code that uses flatMap into a for comprehension
   */

  val random = ZIO.attempt(scala.util.Random.nextInt(3) + 1)

  def printLine5(line: String) = ZIO.attempt(println(line))

  val readLine5 = ZIO.attempt(scala.io.StdIn.readLine())

  random.flatMap { int =>
    printLine5("Guess a number from 1 to 3:").flatMap { _ =>
      readLine5.flatMap { num =>
        if (num == int.toString) printLine5("You guessed right!")
        else printLine5(s"You guessed wrong, the number was $int!")
      }
    }
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    randomNum <- random
    _ <- printLine5("Guess a number from 1 to 3:")
    number <- readLine5
    _ <-
      if (number == randomNum.toString) printLine5("You guessed right!")
      else printLine5(s"You guessed wrong, the number was $randomNum!")
  } yield ()
}
