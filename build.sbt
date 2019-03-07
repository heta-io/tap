// Copyright (C) 2017 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import LocalSbtSettings._

//Project details
lazy val projectName = "tap"
lazy val projectOrg = "io.heta"
lazy val projectVersion = "3.3.0c"

lazy val serverName = s"${projectName}_server"
lazy val clientName = s"${projectName}_client"
lazy val sharedName = s"${projectName}_shared"

//Versions
scalaVersion in ThisBuild := "2.12.8"

//Scala library versions
lazy val nlytxNlpApiV = "1.1.2"
lazy val nlytxNlpExpressionsV = "1.1.2"
lazy val nlytxNlpCommonsV = "1.1.2"
lazy val factorieNlpV = "1.0.4"
lazy val factorieNlpModelsV = "1.0.3"
lazy val cluLabProcessorV = "7.4.4"
lazy val tensorFlowV = "0.4.0"

lazy val sangriaVersion = "1.4.2"
lazy val sangriaJsonVersion = "1.0.5"
lazy val playJsonVersion = "2.7.0"

lazy val akkaVersion = "2.5.19"
lazy val alpakkaVersion = "1.0-M2"
lazy val scalatestVersion = "3.0.5"
lazy val scalatestPlayVersion = "3.1.2"

lazy val vScalaTags = "0.6.7"
lazy val vXmlBind = "2.3.0"
lazy val vUpickle = "0.6.6"

//Java library versions
lazy val openNlpVersion = "1.9.0"
lazy val langToolVersion = "4.4"
lazy val deepLearning4jVersion = "0.9.1"

//ScalaJS
lazy val vScalaJsDom = "0.9.6"
lazy val vWebpack = "4.10.2"
lazy val vWebpackDevServer = "3.1.4"
lazy val vSlinky = "0.5.1"

lazy val vBootstrap = "4.1.3"
lazy val vSjsBootstrap = "2.3.4"
lazy val vJquery = "3.2.1"
lazy val vPopper = "1.14.3"
lazy val vD3 = "5.4.0"

//Settings
val sharedSettings = Seq(
  organization := projectOrg,
  version := projectVersion
)

//Dependencies

val playDeps = Seq(ws, guice, ehcache, specs2 % Test)

val apiDependencies = Seq(
  "org.sangria-graphql" %% "sangria" % sangriaVersion,
  "com.typesafe.play" %% "play-json" % playJsonVersion,
  //"com.typesafe.play" %% "twirl-api" % twirlApiVersion,
  "com.lihaoyi" %% "scalatags" % vScalaTags,
  "org.sangria-graphql" %% "sangria-play-json" % sangriaJsonVersion,
  "org.portable-scala" %% "portable-scala-reflect" % "0.1.0" // Reflection for batch mode
)

val analyticsDependencies = Seq(
  "io.nlytx" %% "nlytx-nlp-api" % nlytxNlpApiV,
  "io.nlytx" %% "nlytx-nlp-expressions" % nlytxNlpExpressionsV,
  "io.nlytx" %% "nlytx-nlp-commons" % nlytxNlpCommonsV,
  "io.nlytx" %% "factorie-nlp" % factorieNlpV,
  "io.nlytx" %% "factorie-nlp-models" % factorieNlpModelsV,
  "org.clulab" %% "processors-main" % cluLabProcessorV,
  "org.clulab" %% "processors-odin" % cluLabProcessorV,
  "org.clulab" %% "processors-modelsmain" % cluLabProcessorV,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion, //Used by cluLabProcessor
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-s3" % alpakkaVersion,
  "org.apache.opennlp" % "opennlp-tools" % openNlpVersion,
  "org.languagetool" % "language-en" % langToolVersion,
  "com.typesafe.play" %% "play-json" % playJsonVersion,
  //"org.platanios" % "tensorflow" % tensorFlowV,  // http://platanios.org/tensorflow_scala/installation.html
  //"org.platanios" % "tensorflow" % tensorFlowV classifier "darwin-cpu-x86_64" //"linux-cpu-x86_64"
)

val dl4jDependencies = Seq(
  "org.deeplearning4j" % "deeplearning4j-core" % deepLearning4jVersion,
  "org.deeplearning4j" % "deeplearning4j-nlp" % deepLearning4jVersion,
  "org.nd4j" % "nd4j-native-platform" % deepLearning4jVersion % Test
)

val loggingDependencies = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
)

val testDependencies = Seq(
  "org.scalactic" %% "scalactic" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % scalatestPlayVersion % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion
)



lazy val tap = project.in(file("."))
  .dependsOn(server,client)
  .aggregate(server,client)
  .settings(
    sharedSettings,
    libraryDependencies ++= playDeps,
    libraryDependencies ++= apiDependencies,

    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),

    dockerExposedPorts := Seq(9000,80), // sbt docker:publishLocal
    dockerRepository := Some(s"$dockerRepoURI"),
    defaultLinuxInstallLocation in Docker := "/opt/docker",
    dockerExposedVolumes := Seq("/opt/docker/logs"),
    dockerBaseImage := "openjdk:11-jdk",

    // sbt-site needs to know where to find the paradox site
    sourceDirectory in Paradox := baseDirectory.value / "documentation",
    // paradox needs a theme
    ParadoxMaterialThemePlugin.paradoxMaterialThemeSettings(Paradox),
    paradoxProperties in Compile ++= Map(
      "github.base_url" -> s"$githubBaseUrl",
      "scaladoc.api.base_url" -> s"$scaladocApiBaseUrl"
    ),
    // Puts unified scaladocs into target/api
    siteSubdirName in ScalaUnidoc := "api",
    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc)

  ).enablePlugins(PlayScala)
  .enablePlugins(WebScalaJSBundlerPlugin)
  .enablePlugins(SbtWeb)
  .enablePlugins(ParadoxSitePlugin, ParadoxMaterialThemePlugin,SiteScaladocPlugin,ScalaUnidocPlugin) // Documentation plugins


lazy val server = (project in file(serverName))
  .settings(
    sharedSettings,
    resolvers += Resolver.bintrayRepo("nlytx", "nlytx-nlp"),
    resolvers += Resolver.jcenterRepo,
    libraryDependencies ++= analyticsDependencies ++ dl4jDependencies ++ loggingDependencies ++ testDependencies,
    libraryDependencies +=  "org.projectlombok" % "lombok" % "1.18.4", //Used by langtool old vers problem with JDK 10 and 11
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := projectOrg,
    buildInfoOptions += BuildInfoOption.BuildTime,
  ).enablePlugins(BuildInfoPlugin)


lazy val client = project.in(file(clientName))
  .settings(
    sharedSettings,
    scalaJSUseMainModuleInitializer := true,
    webpackBundlingMode := BundlingMode.LibraryAndApplication(), //Needed for top level exports
    version in webpack := vWebpack, // Needed for version 4 webpack
    version in startWebpackDevServer := vWebpackDevServer, // Needed for version 4 webpack
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % vScalaJsDom,
      //"org.singlespaced" %%% "scalajs-d3" % "0.3.4",
      "com.github.karasiq" %%% "scalajs-bootstrap-v4" % vSjsBootstrap,
      "com.lihaoyi" %%% "scalatags" % vScalaTags, //Using ScalaTags instead of Twirl
      //"com.lihaoyi" %%% "upickle" % vUpickle, //Using uJson for main JSON
      "me.shadaj" %%% "slinky-core" % vSlinky, // core React functionality, no React DOM
      "me.shadaj" %%% "slinky-web" % vSlinky // React DOM, HTML and SVG tags
    ),
    npmDependencies in Compile ++= Seq(
      "bootstrap" -> vBootstrap,
      //"jquery" -> vJquery, //used by bootstrap
      "popper.js" -> vPopper, //used by bootstrap
      //"d3" -> vD3,
      "react" -> "16.6.1",
      "react-dom" -> "16.6.1",
      "graphiql" -> "0.12.0",
      "graphql" -> "14.0.2",
      "isomorphic-fetch" -> "2.2.1"
    )
  ).enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin, ScalaJSWeb)


//Documentation
//Task for building docs and copying to root level docs folder (for GitHub pages)
val updateDocsTask = TaskKey[Unit]("updateDocs","copies paradox docs to /docs directory")

updateDocsTask := {
  val siteResult = makeSite.value
  val docSource = new File("target/site")
  val docDest = new File("docs")
  IO.copyDirectory(docSource,docDest,overwrite=true,preserveLastModified=true)
}

javaOptions in Universal ++= Seq(
  // -J params will be added as jvm parameters
  "-J-Xmx6g",
  "-J-Xms2g"
)