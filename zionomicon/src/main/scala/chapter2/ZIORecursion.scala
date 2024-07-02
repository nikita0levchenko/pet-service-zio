package chapter2

import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault
import zio.Console

import java.io.IOException

object ZIORecursion extends ZIOAppDefault {

  val readInt: ZIO[Any, Throwable, Int] = for {
    line <- Console.readLine
    int <- ZIO.attempt(line.toInt)
    _ <- Console.printLine(s"Provided digit: $int")
  } yield int

  lazy val readAndRetry: ZIO[Any, IOException, Nothing] = readInt
    .orElse(Console.printLine("Please provide correct digit"))
    .zipRight(readAndRetry)

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- readAndRetry
  } yield ()

}
