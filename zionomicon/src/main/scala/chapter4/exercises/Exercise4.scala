package chapter4.exercises

import zio.Scope
import zio.ZIO
import zio.Console
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise4 extends ZIOAppDefault {

  /*
  Using the ZIO#foldCauseZIO method, which “runs” an effect to an Exit value, implement the following function,
  which will execute the specified effect on any failure at all:
   */

  def onAnyFailure[R, E, A](
      zio: ZIO[R, E, A],
      handler: ZIO[R, E, Any]
  ): ZIO[R, E, A] =
    zio.foldCauseZIO(
      cause => handler *> ZIO.failCause(cause),
      result => ZIO.succeed(result)
    )

  val devideByZero = ZIO.succeed(1 / 0)
  val handler = Console.printLine("_ / 0 - this is wrong")

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- onAnyFailure(devideByZero, handler)
  } yield ()
}
