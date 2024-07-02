package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.Console
import zio.ZIOAppArgs
import zio.ZIOAppDefault

import java.io.IOException

object Exercise19 extends ZIOAppDefault {

  /*
  Using the Console service and recursion, write a function that will repeatedly read input from the console
  until the specified user-defined function evaluates to true on the input.
   */

  def readUntil(
      acceptInput: String => Boolean
  ): ZIO[Any, IOException, String] = {
    println("Enter a value:")
    Console.readLine.flatMap { line =>
      if (acceptInput(line)) {
        println("Value does satisfy be predicate")
        ZIO.succeed(line)
      } else {
        println("Value doesn't satisfy by predicate")
        readUntil(acceptInput)
      }
    }
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    res <- readUntil(_ == "some")
  } yield ()
}
