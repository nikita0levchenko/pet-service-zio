package chapter4.exercises

import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise1 extends ZIOAppDefault {

  /*
  Using the appropriate effect constructor, fix the following function so that it no longer fails with defects when executed.
  Make a note of how the inferred return type for the function changes.
   */

  def failWithMessage(string: String) = ZIO.attempt(throw new Error(string))

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- failWithMessage("some error")
  } yield ()
}
