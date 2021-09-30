name := "json-schema-build"
ThisBuild / organization := "json-schema"
ThisBuild / organizationName := "just playing around"
ThisBuild / versionScheme := Some("early-semver")

val supportedScalaVersions = Seq("2.12.13", "2.13.5", "3.0.2")

lazy val root = (project in file("."))
  .aggregate(`json-schema`)
  .aggregate(`json-schema-derivation`)
  .aggregate(`json-schema-compatibility`)
  .settings(crossScalaVersions := Nil)
  .settings(publish / skip := true)

val commonSettings = Seq(
  crossScalaVersions := supportedScalaVersions,
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.9" % Test,
    "org.scalatest" %% "scalatest-flatspec" % "3.2.9" % Test,
    "org.scalatestplus" %% "scalacheck-1-15" % "3.2.9.0" % Test
  )
)

val `json-schema` = project
  .settings(commonSettings)

val `json-schema-derivation` = project
  .settings(commonSettings)
  .dependsOn(`json-schema`)

val `json-schema-compatibility` = project
  .settings(commonSettings)
  .dependsOn(`json-schema`)

// Reloads the SBT build when source changes in this file are detected.
Global / onChangedBuildSource := ReloadOnSourceChanges
