package chapter3

import zio.Scope
import zio.ZIO
import zio.Console
import zio.Random
import zio.durationInt
import zio.test.Assertion.equalTo
import zio.test.Spec
import zio.test.TestConsole
import zio.test.TestEnvironment
import zio.test.ZIOSpecDefault
import zio.test._

object TestServices extends ZIOSpecDefault {

  val greet: ZIO[Any, Nothing, Unit] = for {
    name <- Console.readLine.orDie
    _ <- Console.printLine(s"Hello, $name!").orDie
  } yield ()

  val goShopping: ZIO[Any, Nothing, Unit] =
    Console.printLine("Going shopping!").orDie.delay(1.hour)

  // An integer value generator
  val intGen: Gen[Any, Int] = Gen.int

  // Create generator for custom data type
  final case class User(name: String, age: Int)

  val aNameGen: Gen[Random with Sized, String] = Gen.asciiString

  val aAgeGen: Gen[Random, Int] = Gen.int(18, 120)

  val aUserGen: Gen[Random with Sized, User] = for {
    name <- aNameGen
    age <- aAgeGen
  } yield User(name, age)

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Test services like console read/write")(
      test("Check greet effect behaviour") {
        for {
          _ <- TestConsole.feedLines("Some Name")
          _ <- greet
          value <- TestConsole.output
        } yield assert(value)(equalTo(Vector("Hello, Some Name!\n")))
      },
      test("goShopping delays for one hour") {
        for {
          fiber <- goShopping.fork
          _ <- TestClock.adjust(1.hour)
          _ <- fiber.join
        } yield assertCompletes
      },
      test("integer addition is associative") {
        check(intGen, intGen, intGen) { (x, y, z) =>
          val left = (x + y) + z
          val right = x + (y + z)
          assert(left)(equalTo(right))
        }
      }
    )
}
