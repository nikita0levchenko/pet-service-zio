package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.Console
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise14 extends ZIOAppDefault {

  /*
  Using ZIO.async, convert the following asynchronous, callback-based func- tion into a ZIO function:
   */

  val dummyDB = Map("key1" -> 1, "key2" -> 2)

  def getCacheValue(
      key: String,
      onSuccess: String => Unit,
      onFailure: Throwable => Unit
  ): Unit = {
    dummyDB.get(key) match {
      case Some(value) => onSuccess(value.toString)
      case None        => onFailure(new NoSuchElementException)
    }
  }

  def getCacheValueZio(key: String): ZIO[Any, Throwable, String] =
    ZIO.async[Any, Throwable, String] { callback =>
      getCacheValue(
        key,
        value => callback(ZIO.succeed(value)),
        err => callback(ZIO.fail(err))
      )
    }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    res <- getCacheValueZio("key3")
    _ <- Console.printLine(res)
  } yield ()
}
