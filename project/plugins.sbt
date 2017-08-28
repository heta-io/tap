resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.3")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.1")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.2")
//addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.2.1")
//addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.0")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")