package chapter3

import org.scalatest.funsuite.AnyFunSuite
import zio.ZIO
import zio.Runtime
import zio.Unsafe

class ZIOCasualTestingSpec extends AnyFunSuite {

  def unsafeRun[E, A](zio: ZIO[Any, E, A]): A = Unsafe.unsafe {
    implicit unsafe =>
      Runtime.default.unsafe.run(zio).getOrThrowFiberFailure()
  }

  ignore("ZIO.attempt() can't be equal to Int") {
    /*
    ZIO.attempt - is blueprint of computation it can't be correct to compare ZIO and Int types
     */
    assert(ZIO.attempt(1 + 2) == 2)
  }

  test("evaluating ZIO.attempt() should be equal to Int") {
    assert(unsafeRun(ZIO.attempt(1 + 1)) == 2)
  }

}
