package chapter4

import zio.CanFail.canFailAmbiguous1
import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object ZIOErrorModel extends ZIOAppDefault {

  /*

  All troubles can be separated on Errors and Defects:

  Error - something what we can predict and represent with the Throwable type, for example NPE
  Defects - rest unpredictable things

  ZIO[R, E, A] definitely is ZIO[R, Cause[E], A]

  Here the Cause type implementation:
   */

  sealed trait Cause[+E]

  object Cause {
    final case class Fail[+E](e: E) extends Cause[E] // this is Error
    final case class Die(t: Throwable) extends Cause[Nothing] // this is Defect
  }

  /*
  Another conception what we have - Exit type
  Exit - this is the type, describes all the different ways that running effects can finish execution

  This can be similar with Either[+E, +A] type
   */

  sealed trait Exit[+E, +A]

  object Exit {
    final case class Success[+A](result: A) extends Exit[Nothing, A]
    final case class Failure[+E](cause: E) extends Exit[E, Nothing]
  }

  /*
  In ZIO we have sandbox: ZIO[R, E, A] => ZIO[R, Cause[E], A] to catch not only errors but defects to
  And reverse operation: unsandbox: ZIO[R, Cause[E], A] => ZIO[R, E, A]

  Also we can fold sandboxed effects with foldCauseZIO[R1 <: R, E1, B]: ZIO[R1, E1, B]
   */

  // Convert errors to defect
  val divByZero = ZIO.succeed(1 / 0).orDie
  val particularDevideByZero: ZIO[Any, Throwable, Int] =
    ZIO.attempt(1 / 0).refineOrDie { case e: ArithmeticException => e }

  /*
  a.orElse(a)  - run a effect, if a - fails then run b effect
  You can change error type with mapError() function
  ZIO.some: ZIO[R, E, Option[A]] => ZIO[R, Option[E], A]
  unsome reverse the some
   */

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- divByZero
    _ <- particularDevideByZero
  } yield ()
}
