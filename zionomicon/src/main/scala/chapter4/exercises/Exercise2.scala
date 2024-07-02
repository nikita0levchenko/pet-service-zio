package chapter4.exercises

import zio.Scope
import zio.ZIO
import zio.Console
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise2 extends ZIOAppDefault {

  /*
  Using the ZIO#foldCauseZIO operator and the Cause#defects method, implement the following function. This function should take the effect,
  inspect defects, and if a suitable defect is found, it should recover from the error with the help of the specified function,
  which generates a new success value for such a defect.
   */

  def recoverFromSomeDefects[R, E, A](
      zio: ZIO[R, E, A]
  )(f: Throwable => Option[A]): ZIO[R, E, A] = zio.foldCauseZIO(
    cause =>
      cause.defects
        .collectFirst(Function.unlift(f))
        .fold[ZIO[R, E, A]](ZIO.failCause(cause))(elem => ZIO.succeed(elem)),
    result => ZIO.succeed(result)
  )

  val devideByZero = ZIO.succeed(1 / 0)
  val recoverFunction: Throwable => Option[Int] = {
    case _: ArithmeticException => Some(Int.MinValue)
    case _                      => Some(0)
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    result <- recoverFromSomeDefects(devideByZero)(recoverFunction)
    _ <- Console.printLine(result)
  } yield ()
}
