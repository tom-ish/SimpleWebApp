name := """simple-web-app"""

herokuAppName in Compile := "simple-web-app"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.13.0"

val playSlickVersion = "4.0.2"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
  "org.postgresql" % "postgresql" % "42.2.6",
  "com.typesafe.play" %% "play-slick" % playSlickVersion,
  "com.typesafe.play" %% "play-slick-evolutions" % playSlickVersion,
  "com.adrianhurt" %% "play-bootstrap" % "1.5.1-P27-B4"
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Xfatal-warnings"
)
