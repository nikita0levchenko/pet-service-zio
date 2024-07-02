package chapter3

import zio.Scope
import zio.ZIO
import zio.test.Assertion.equalTo
import zio.test._

object ZIOTestingSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Example spec")(
      test("Test with ZIO test lib") {
        assert(1 + 1)(equalTo(2))
      },
      test("ZIO.attempt() should be tested properly") {
        assertZIO(ZIO.succeed(1 + 1))(equalTo(2))
      },
      test("Test effects without AssertZIO with map") {
        ZIO.attempt(1 + 1).map(res => assert(res)(equalTo(2)))
      },
      test("Test effects without AssertZIO with for") {
        for {
          res <- ZIO.attempt(1 + 1)
        } yield assert(res)(equalTo(2))
      },
      test("Use logical conjunction and disjunction in tests") {
        for {
          res <- ZIO.attempt(1 + 1)
        } yield assert(res)(equalTo(2)) && assert(res > 0)(equalTo(true))
      }
    )
}

