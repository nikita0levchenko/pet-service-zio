package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.Console
import zio.ZIOAppArgs
import zio.ZIOAppDefault

import scala.collection.mutable
import scala.util.Failure
import scala.util.Success
import scala.util.Try

object Exercise15 extends ZIOAppDefault {

  /*
  15. Using ZIO.async, convert the following asynchronous, callback-based func- tion into a ZIO function:
   */

  trait User

  case class DefaultUser(name: String) extends User

  val dummyDB: mutable.Map[String, User] =
    mutable.Map("key1" -> DefaultUser("name1"), "key2" -> DefaultUser("name2"))

  def saveUserRecord(
      user: User,
      onSuccess: () => Unit,
      onFailure: Throwable => Unit
  ): Unit = Try(dummyDB += ("newKey" -> user)) match {
    case Success(_)         => onSuccess()
    case Failure(exception) => onFailure(exception)
  }

  def saveUserRecordZio(user: User): ZIO[Any, Throwable, Unit] = ZIO.async {
    callback =>
      saveUserRecord(
        user,
        () => callback(ZIO.succeed(println("Value was added successfully"))),
        err => callback(ZIO.fail(err))
      )
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- saveUserRecordZio(DefaultUser("name3"))
    _ <- Console.printLine(dummyDB.toString())
  } yield ()
}
