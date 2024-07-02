package chapter2.exercises

import chapter2.exercises.Exercise1.readFileZio
import zio.ZIO
import zio.Console
import zio.Scope
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise10 extends ZIOAppDefault {
  /*
  Using the following code as a foundation, write a ZIO application that prints out the contents of whatever files
  are passed into the program as command-line arguments.
  You should use the function readFileZio that you developed in these exercises, as well as ZIO.foreach.
   */

  val filePaths = List(
    "/Users/n.o.levchenko/IdeaProjects/zio-practice/src/main/resources/checkFile1.txt",
    "/Users/n.o.levchenko/IdeaProjects/zio-practice/src/main/resources/checkFile2.txt"
  )

  def cat(files: List[String]): ZIO[Any, Throwable, List[Unit]] =
    ZIO.foreach(files)(file =>
      readFileZio(file)
        .flatMap(elem => Console.printLine(elem))
    )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- cat(filePaths)
  } yield ()
}
