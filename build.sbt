name := "opendota-rankings"
version := "1.0"
organization := "io.opendota"
scalaVersion := "2.12.0"
crossScalaVersions := Seq("2.11.8", "2.12.0")

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-core" % "3.5.0",
  "org.json4s" %% "json4s-jackson" % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc" % "2.5.0",
  "org.postgresql" % "postgresql" % "9.4.1212",
  "org.scalaj" %% "scalaj-http" % "2.3.0"
)
    