name := """play-scala-seed"""
organization := "Tushar"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
      guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  // Enable reactive mongo for Play 2.7
  "org.reactivemongo" %% "play2-reactivemongo" % "0.19.1-play27",
  // Provide JSON serialization for reactive mongo
  "org.reactivemongo" %% "reactivemongo-play-json-compat" % "0.19.2-play27",
  "org.reactivemongo" %% "reactivemongo-play-json" % "0.19.1-play27",
  // Provide BSON serialization for reactive mongo
  "org.reactivemongo" %% "reactivemongo-bson-compat" % "0.19.1",
  "org.reactivemongo" %% "reactivemongo-bson-api" % "0.19.1",
  // Provide JSON serialization for Joda-Time
  "com.typesafe.play" %% "play-json-joda" % "2.7.4",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.5"
)