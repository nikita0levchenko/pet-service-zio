ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "zionomicon"
  )

libraryDependencies += "dev.zio" %% "zio" % "2.1.4"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"
libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test" % "2.1.1",
  "dev.zio" %% "zio-test-sbt" % "2.1.4"
)
