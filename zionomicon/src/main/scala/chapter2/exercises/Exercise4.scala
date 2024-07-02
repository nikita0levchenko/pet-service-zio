package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise4 extends ZIOAppDefault {

  /*
  4. Rewrite the following ZIO code that uses flatMap into a for comprehension.
   */

  def printLine(line: String) = ZIO.attempt(println(line))

  val readLine = ZIO.attempt(scala.io.StdIn.readLine())

  printLine("What is your name?").flatMap(_ =>
    readLine.flatMap(name => printLine(s"Hello, $name!"))
  )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- printLine("What is your name?")
    name <- readLine
    _ <- printLine(s"Hello, $name!")
  } yield ()
}
