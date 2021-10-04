name := "json-schema-build"
ThisBuild / organization := "json-schema"
ThisBuild / organizationName := "just playing around"
ThisBuild / versionScheme := Some("early-semver")

val supportedScalaVersions = Seq("2.12.13", "2.13.5", "3.0.2")

lazy val root = (project in file("."))
  .aggregate(`json-schema`)
  .aggregate(`json-schema-derivation`)
  .aggregate(`json-schema-compatibility`)
  .aggregate(`json-schema-serialization-circe`)
  .settings(crossScalaVersions := Nil)
  .settings(publish / skip := true)

val commonSettings = Seq(
  crossScalaVersions := supportedScalaVersions,
  libraryDependencies ++= Seq(
    Dependencies.scalatest % Test,
    Dependencies.scalatestFlatspec % Test,
    Dependencies.scalatestplusScalacheck % Test
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

val `json-schema-serialization-circe` = project
  .settings(commonSettings)
  .settings(libraryDependencies ++= Seq(
    Dependencies.circeCore,
    Dependencies.circeGeneric
  ))
  .dependsOn(`json-schema`)

// Reloads the SBT build when source changes in this file are detected.
Global / onChangedBuildSource := ReloadOnSourceChanges
