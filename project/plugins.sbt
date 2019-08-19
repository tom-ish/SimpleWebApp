resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.3")
addSbtPlugin("com.heroku" % "sbt-heroku" % "2.1.2")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0")