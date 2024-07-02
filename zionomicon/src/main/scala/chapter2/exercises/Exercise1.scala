package chapter2.exercises

import zio.ZIO
import zio.Console
import zio.Scope
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise1 extends ZIOAppDefault {
  /*
  1. Implement a ZIO version of the function readFile by using the ZIO.attempt constructor.
   */

  val ex1FilePath =
    "/Users/n.o.levchenko/IdeaProjects/zio-practice/src/main/resources/checkFile1.txt"

  def readFile(file: String): String = {
    val source = scala.io.Source.fromFile(file)
    try source.getLines().mkString
    finally source.close()
  }

  def readFileZio(file: String): ZIO[Any, Throwable, String] =
    ZIO.attempt(readFile(file))

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    lines <- ZIO.attempt(
      readFile(ex1FilePath)
    )
    zioLines <- readFileZio(ex1FilePath)
    _ <- Console.printLine(lines == zioLines)
  } yield ()
}
