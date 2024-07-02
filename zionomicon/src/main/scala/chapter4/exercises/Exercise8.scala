package chapter4.exercises

import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise8 extends ZIOAppDefault {

  /*
  Using the ZIO#foldZIO method, implement the following two functions, which make working with Either values easier,
  by shifting the unexpected case into the error channel (and reversing this shifting).
   */

  def right[R, E, A, B](
      zio: ZIO[R, E, Either[A, B]]
  ): ZIO[R, Either[E, A], B] =
    zio.foldZIO(
      failure => ZIO.fail(Left(failure)),
      success => success.fold(l => ZIO.fail(Right(l)), r => ZIO.succeed(r))
    )

  def unright[R, E, A, B](
      zio: ZIO[R, Either[E, A], B]
  ): ZIO[R, E, Either[A, B]] =
    zio.foldZIO(
      failure => failure.fold(l => ZIO.fail(l), r => ZIO.succeed(Left(r))),
      success => ZIO.succeed(Right(success))
    )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ZIO.unit
}
