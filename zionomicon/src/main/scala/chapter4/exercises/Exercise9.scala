package chapter4.exercises

import zio.Cause
import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise9 extends ZIOAppDefault {

  /*
  Using the ZIO#sandbox method, implement the following function.
   */

  def catchAllCause[R, E1, E2, A](
      zio: ZIO[R, E1, A],
      handler: Cause[E1] => ZIO[R, E2, A]
  ): ZIO[R, E2, A] = zio.sandbox.foldZIO(
    failure => handler(failure),
    success => ZIO.succeed(success)
  )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ZIO.unit
}
