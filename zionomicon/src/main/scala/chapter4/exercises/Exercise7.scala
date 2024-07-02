package chapter4.exercises

import zio.Exit
import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise7 extends ZIOAppDefault {

  /*
  Using the ZIO#foldZIO method, implement the following two functions,
  which make working with Either values easier, by shifting the unexpected case into the error channel
  (and reversing this shifting).
   */

  def left[R, E, A, B](
      zio: ZIO[R, E, Either[A, B]]
  ): ZIO[R, Either[E, B], A] =
    zio.foldZIO(
      error => ZIO.fail(Left(error)),
      success => success.fold(a => ZIO.succeed(a), b => ZIO.fail(Right(b)))
    )

  def unleft[R, E, A, B](
      zio: ZIO[R, Either[E, B], A]
  ): ZIO[R, E, Either[A, B]] =
    zio.foldZIO(
      error => error.fold(a => ZIO.fail(a), b => ZIO.succeed(Right(b))),
      success => ZIO.succeed(Left(success))
    )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ZIO.unit
}
