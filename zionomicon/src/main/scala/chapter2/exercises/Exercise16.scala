package chapter2.exercises

import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise16 extends ZIOAppDefault {

  /*
  Using ZIO.fromFuture, convert the following code to ZIO:
   */

  import scala.concurrent.{ExecutionContext, Future}

  trait Query {
    def runQuery: Result
  }
  case class UserQuery(id: Int) extends Query {
    override def runQuery: Result = dummyDB.find(_.id == id) match {
      case Some(user) => UserResult(user)
      case None       => NotFoundResult
    }
  }

  case class User(id: Int, name: String)

  trait Result

  case class UserResult(user: User) extends Result

  case object NotFoundResult extends Result

  val dummyDB = List(User(1, "name1"), User(2, "name2"))

  def doQuery(query: Query)(implicit ec: ExecutionContext): Future[Result] =
    Future(query.runQuery)

  def doQueryZio(query: Query): ZIO[Any, Throwable, Result] = ZIO.fromFuture {
    implicit ec =>
      doQuery(query)(ec)
  }

  val simpleUserQuery: UserQuery = UserQuery(1)

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- doQueryZio(simpleUserQuery)
  } yield ()
}
