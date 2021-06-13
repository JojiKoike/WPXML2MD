lazy val root = (project in file(".")).settings(
  name := "WP_XML2MD",
  organization := "com.startappdevfron35",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.13.6",
  scalacOptions ++= Seq("-deprecation", "-feature", "-language:implicitConversions", "-Ywarn-unused:imports"),
  libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-xml" % "2.0.0",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
  )
)
