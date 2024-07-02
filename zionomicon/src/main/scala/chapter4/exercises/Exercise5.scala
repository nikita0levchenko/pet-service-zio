package chapter4.exercises

import zio.Scope
import zio.ZIO
import zio.Console
import zio.ZIOAppArgs
import zio.ZIOAppDefault

import java.io.IOException

object Exercise5 extends ZIOAppDefault {

  /*
  Using the ZIO#refineOrDie method, implement the ioException func- tion,
  which refines the error channel to only include the IOException error.
   */

  def ioException[R, A](
      zio: ZIO[R, Throwable, A]
  ): ZIO[R, java.io.IOException, A] =
    zio.refineOrDie { case e: IOException =>
      e
    }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- ioException(Console.readLine)
  } yield ()
}
