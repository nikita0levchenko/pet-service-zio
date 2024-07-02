package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.Console
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise17 extends ZIOAppDefault {

  /*
  Using the Console, write a little program that asks the user what their name is, and then prints it out to them with a greeting.
   */

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- Console.printLine("What is yor name?\nEnter your name below")
    name <- Console.readLine
    _ <- Console.printLine(s"Greetings $name")
  } yield ()
}
