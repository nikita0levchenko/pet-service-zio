package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.Console
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise13 extends ZIOAppDefault {

  /*
  Using ZIO.succeed, convert the following procedural function into a ZIO
  function:
   */
  def currentTime(): Long = java.lang.System.currentTimeMillis()

  def zioCurrentTime: ZIO[Any, Nothing, Long] =
    ZIO.succeed(java.lang.System.currentTimeMillis())

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    time <- zioCurrentTime
    _ <- Console.printLine(s"Current time: $time")
  } yield ()
}
