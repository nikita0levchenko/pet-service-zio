package chapter2

import zio.Scope
import zio.Task
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault
import zio.durationInt

import scala.io.StdIn

object ZioPlayground extends ZIOAppDefault {

  val goShopping: Task[Unit] = ZIO.attempt(println("Going to the store"))

  val goSgoppingLater = goShopping.delay(1.minute)

  // Sequential composition

  val readLine = ZIO.attempt(StdIn.readLine())

  val printLine = (line: String) => ZIO.attempt(println(line))

  val echo = readLine.flatMap(line => printLine(line))

  // zipWith
  val firstName = ZIO.attempt(StdIn.readLine("What is your first name?"))

  val lastName = ZIO.attempt(StdIn.readLine("What is your last name?"))

  val fullName = (firstName zipWith lastName)((first, last) => s"$first $last")

  val helloWorld =
    ZIO.attempt(print("Hello, ")) *> ZIO.attempt(print("World\n"))

  val printNumbers = ZIO.foreach(1 to 100) { n =>
    printLine(n.toString)
  }

  val prints = List(
    printLine("The"),
    printLine("quick"),
    printLine("brown"),
    printLine("fox")
  )

  val printWords = ZIO.collectAll(prints)

  /*
  ZIO.attempt take side effect code and convert it into pure value
   */

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    //_ <- goShopping
    //_ <- echo
    //_ <- fullName.map(name => println(name))
    //_ <- helloWorld
    //_ <- printNumbers
    _ <- printWords
  } yield ()
}
