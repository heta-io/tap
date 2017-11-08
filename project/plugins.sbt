resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

//Play framework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.7")

//Dependency management
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.3")
//addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

//Code quality
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
//addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

//Build and packaging
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.2")
//addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")

//Documentation
//addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.3.1")
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox" % "0.3.1")
//addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.2")

//Other
