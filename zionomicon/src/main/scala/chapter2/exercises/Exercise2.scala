package chapter2.exercises

import chapter2.exercises.Exercise1.readFileZio
import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault
import zio.Console
import java.io._

object Exercise2 extends ZIOAppDefault {
  /*
  2. Implement a ZIO version of the function writeFile by using the
  ZIO.attempt constructor.
   */

  val ex2FilePath =
    "/Users/n.o.levchenko/IdeaProjects/zio-practice/src/main/resources/checkFile1.txt"

  def writeFile(file: String, text: String): Unit = {
    val pw = new PrintWriter(new File(file))
    try pw.write(text)
    finally pw.close
  }

  def writeFileZio(
      file: String,
      text: String
  ): ZIO[Any, Throwable, Unit] =
    ZIO.attempt(writeFile(file, text))

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- writeFileZio(ex2FilePath, "some text")
    zioEx2FileLines <- readFileZio(ex2FilePath)
    _ <- Console.printLine(zioEx2FileLines)
  } yield ()
}
