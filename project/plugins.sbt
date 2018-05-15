resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

//Play framework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.13")

//ScalaJS
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.22")
addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.12.0")
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.7")
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.4")

//Dependency management
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")
//addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

//Code quality
//addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
//addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

//Build and packaging
//addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.2")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

//Documentation
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox" % "0.3.3")
