ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

val blazeVersion = "0.23.14"
val http4sVersion = "0.23.19-RC3"

lazy val root = (project in file("."))
  .settings(
    name := "contract-checker"
  )

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % blazeVersion,
  "org.http4s" %% "http4s-blaze-client" % blazeVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
)