package chapter3

import zio.Scope
import zio.ZIO
import zio.test.Assertion.fails
import zio.test.Assertion.hasSameElements
import zio.test.Assertion.isDistinct
import zio.test.Assertion.isNonEmptyString
import zio.test.Assertion.isSorted
import zio.test.Assertion.not
import zio.test._
import zio.test.TestEnvironment
import zio.test.ZIOSpecDefault

object AnotherAssertions extends ZIOSpecDefault {

  // Create custom assertions
  val customAssertion: Assertion[List[Int]] = not(isDistinct) && isSorted

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Check another assertions in ZIO")(
      test("Two lists has the same elements") {
        assert(List(1, 2, 3, 4))(hasSameElements(List(4, 3, 2, 1)))
      },
      test("Fails with particular type") {
        for {
          exit <- ZIO
            .attempt(1 / 0)
            .catchAll(_ => ZIO.fail("Can't divide by ero"))
            .exit
        } yield assert(exit)(fails(isNonEmptyString))
      }
    )
}
