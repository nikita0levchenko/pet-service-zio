package chapter4.exercises

import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise3 extends ZIOAppDefault {

  /*
  Using the ZIO#foldCauseZIO operator and the Cause#prettyPrint method, implement an operator that takes an effect,
  and returns a new effect that logs any failures of the original effect (including errors and defects),
  without changing its failure or success value.
   */

  def logFailures[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, A] = zio.foldCauseZIO(
    cause =>
      ZIO.succeed(println(cause.prettyPrint)) zipRight ZIO.failCause(cause),
    result => ZIO.succeed(result)
  )

  val devideByZero = ZIO.succeed(1 / 0)

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- logFailures(devideByZero)
  } yield ()
}
