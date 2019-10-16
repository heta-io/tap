resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

//Play framework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.2")

//ScalaJS
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.28")
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.9-0.6")
addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.15.0-0.6")

//Dependency management
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.4.0")
//addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

//Build and packaging
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.22")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

//Documentation
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.4.0")
addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.4.2")

//Code quality
//addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
//addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")


