name := """simple-web-app"""

herokuAppName in Compile := "simple-web-app"

val buildVersion = "0.18.0"

version := "1.0-SNAPSHOT"

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  "Sonatype Staging" at "https://oss.sonatype.org/content/repositories/staging/")

scalacOptions ++= Seq(
  "-encoding", "UTF-8", "-target:jvm-1.8",
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xlint",
  "-g:vars"
  //"-Xfatal-warnings"
)

scalaVersion := "2.13.0"

libraryDependencies ++= {
  val os = sys.props.get("os.name") match {
    case Some(osx) if osx.toLowerCase.startsWith("mac") =>
      "osx"

    case _ =>
      "linux"
  }

  val (playVer, nativeVer) = buildVersion.span(_ != '-') match {
    case (major, "") =>
      s"${major}-play27" -> s"${major}-${os}-x86-64"

    case (major, mod) =>
      s"${major}-play27${mod}" -> s"${major}-${os}-x86-64${mod}"
  }

  Seq(
    guice,
    //"com.typesafe.play" %% "play-iteratees" % "2.6.1",
    "com.typesafe.akka" %% "akka-slf4j" % "2.6.0-M3",
    "org.reactivemongo" %% "play2-reactivemongo" % playVer,
    "org.reactivemongo" % "reactivemongo-shaded-native" % nativeVer,

    "com.adrianhurt" %% "play-bootstrap" % "1.5.1-P27-B4",
    "org.mindrot" % "jbcrypt" % "0.3m"
  )
}

routesGenerator := InjectedRoutesGenerator

fork in run := true

lazy val root = (project in file(".")).enablePlugins(PlayScala)