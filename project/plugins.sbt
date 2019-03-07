resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

//Play framework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.0")

//ScalaJS
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.26")
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.8-0.6")
addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.14.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.14.0")


//Dependency management
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")
//addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

//Build and packaging
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.15")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

//Documentation
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.3.2")
addSbtPlugin("io.github.jonas" % "sbt-paradox-material-theme" % "0.5.1")
addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.4.1")

//Code quality
//addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
//addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")


