ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "Taller 5"
  )
/*libraryDependencies ++= Seq(
  ("com.storm-enroute"%%"scalameter-core"%"0.21").cross(CrossVersion.for3Use2_13),
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.3",
  "org.scalameta" %% "munit" % "0.7.26" % Test
)*/
libraryDependencies ++= Seq(
)
libraryDependencies += "com.storm-enroute" %% "scalameter-core" % "0.21"
libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.21" % "test"