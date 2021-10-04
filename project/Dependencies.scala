import sbt._

object Dependencies {
  // https://github.com/scalatest/scalatest
  val scalatestVersion = "3.2.9"
  val scalatest = "org.scalatest" %% "scalatest" % scalatestVersion
  val scalatestFlatspec = "org.scalatest" %% "scalatest-flatspec" % scalatestVersion
  val scalatestplusScalacheck = "org.scalatestplus" %% "scalacheck-1-15" % s"${scalatestVersion}.0"

  // https://github.com/circe/circe
  val circeVersion = "0.14.1"
  val circeCore = "io.circe" %% "circe-core" % circeVersion
  val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
}
