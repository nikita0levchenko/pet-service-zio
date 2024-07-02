package chapter2.exercises

import chapter2.exercises.Exercise1.readFile
import chapter2.exercises.Exercise1.readFileZio
import chapter2.exercises.Exercise2.ex2FilePath
import chapter2.exercises.Exercise2.writeFile
import chapter2.exercises.Exercise2.writeFileZio
import zio.Scope
import zio.ZIO
import zio.ZIOAppArgs
import zio.ZIOAppDefault

object Exercise3 extends ZIOAppDefault {

  /*
  3. Using the flatMap method of ZIO effects, together with the readFileZio and writeFileZio functions that you wrote,
  implement a ZIO version of the function copyFile.
   */

  val ex3FilePath =
    "/Users/n.o.levchenko/IdeaProjects/zio-practice/src/main/resources/checkFile2.txt"

  def copyFile(source: String, dest: String): Unit = {
    val contents = readFile(source)
    writeFile(dest, contents)
  }

  def copyFileZio(
      source: String,
      dest: String
  ): ZIO[Any, Throwable, Unit] =
    readFileZio(source).flatMap(lines => writeFileZio(dest, lines))

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- copyFileZio(ex2FilePath, ex3FilePath)
  } yield ()
}
