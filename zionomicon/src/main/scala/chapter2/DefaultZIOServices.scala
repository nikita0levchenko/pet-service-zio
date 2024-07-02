package chapter2

import zio.Clock
import zio.Scope
import zio.UIO
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault
import zio.Console
import zio.IO
import zio.System

import java.io.IOException
import java.time.OffsetDateTime

object DefaultZIOServices extends ZIOAppDefault {

  // Clock service
  val currentTime: UIO[OffsetDateTime] = Clock.currentDateTime

  // Console service
  val readSomething: IO[IOException, String] =
    Console.readLine("Enter something here")

  val justPrintLine = line => Console.printLine(line)

  val justPrint: IO[IOException, Unit] = Console.print("some line2")

  // System service
  val someEnv = System.env("USER")

  val loglEvelProp = System.property("LOG_LEVEL")

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- currentTime.map(println)
    res <- readSomething
    _ <- justPrintLine(res)
    _ <- justPrint
    user <- someEnv
    _ <- user match {
      case Some(value) => Console.printLine(s"The USER is: $value")
      case None        => Console.printLine("There is no USER env variable set")
    }
    level <- loglEvelProp
    _ <- level match {
      case Some(value) => Console.printLine(s"LOG_LEVEL prop is: $value")
      case None        => Console.printLine("There is no LOG_LEVEL")
    }
  } yield ()
}
