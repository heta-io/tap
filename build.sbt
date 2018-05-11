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
lazy val projectVersion = "3.2.1"
lazy val projectScalaVersion = "2.12.6"

lazy val serverName = "tap"
lazy val clientName = "clientJS"
lazy val sharedName = "sharedJS"

//lazy val serverVersion = projectVersion
lazy val clientVersion = projectVersion
lazy val sharedVersion = projectVersion

//Scala library versions
lazy val nlytxNlpApiV = "1.1.0"
lazy val nlytxNlpExpressionsV = "1.0.0"
lazy val nlytxNlpCommonsV = "1.0.0"
lazy val factorieNlpV = "1.0.4"
lazy val factorieNlpModelsV = "1.0.3"
lazy val cluLabProcessorV = "7.2.2"

lazy val sangriaVersion = "1.4.0"
lazy val sangriaJsonVersion = "1.0.4"
lazy val playJsonVersion = "2.6.9"
lazy val scalaTagsVersion = "0.6.7"

lazy val akkaStreamVersion = "2.5.12"
lazy val scalatestVersion = "3.0.5"
lazy val scalatestPlayVersion = "3.1.2"

//Java library versions
lazy val openNlpVersion = "1.8.4"
lazy val langToolVersion = "4.1"
lazy val deepLearning4jVersion = "0.9.1"

//ScalaJS
lazy val scalaJsDomVersion = "0.9.5"
lazy val scalaJsD3Version = "0.3.4"
lazy val scalaJsBootstrapVersion = "2.3.1"


val apiDependencies = Seq(
  "org.sangria-graphql" %% "sangria" % sangriaVersion,
  "com.typesafe.play" %% "play-json" % playJsonVersion,
  //"com.typesafe.play" %% "twirl-api" % twirlApiVersion,
  "com.lihaoyi" %% "scalatags" % scalaTagsVersion,
  "org.sangria-graphql" %% "sangria-play-json" % sangriaJsonVersion
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
  "com.typesafe.akka" % "akka-stream_2.12" % akkaStreamVersion,
  "org.apache.opennlp" % "opennlp-tools" % openNlpVersion,
  "org.languagetool" % "language-en" % langToolVersion,
  "com.typesafe.play" %% "play-json" % playJsonVersion,
)

val dl4jDependencies = Seq(
  "org.deeplearning4j" % "deeplearning4j-core" % deepLearning4jVersion,
  "org.deeplearning4j" % "deeplearning4j-nlp" % deepLearning4jVersion,
  "org.nd4j" % "nd4j-native-platform" % deepLearning4jVersion % Test
)

val loggingDependencies = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
)

val testDependencies = Seq(
  "org.scalactic" %% "scalactic" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % scalatestPlayVersion % "test",
  "com.typesafe.akka" % "akka-stream-testkit_2.12" % akkaStreamVersion
)

//Modules

lazy val commonSettings = Seq(
  scalaVersion := projectScalaVersion,
  organization := projectOrg
)

lazy val play = (project in file("."))
  .settings(
    commonSettings,
    name := projectName,
    version := projectVersion,
    scalaJSProjects := Seq(clientJS),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++= Seq(ws, guice),
    libraryDependencies ++= apiDependencies,
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value,
    //PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value,
    dockerExposedPorts := Seq(9000,80), // sbt docker:publishLocal
    dockerRepository := Some(s"$dockerRepoURI"),
    defaultLinuxInstallLocation in Docker := "/opt/docker",
    dockerExposedVolumes := Seq("/opt/docker/logs"),
    dockerBaseImage := "openjdk:9-jdk"
  ).enablePlugins(PlayScala,JavaAppPackaging)
  //.disablePlugins(PlayLayoutPlugin)
  .dependsOn(server,sharedJvm)
  .aggregate(server)

lazy val server = (project in file(serverName))
  .settings(
    resolvers += Resolver.bintrayRepo("nlytx", "nlytx-nlp"),
    libraryDependencies ++= analyticsDependencies ++ dl4jDependencies ++ loggingDependencies ++ testDependencies,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := projectOrg,
    buildInfoOptions += BuildInfoOption.BuildTime,
  ).enablePlugins(BuildInfoPlugin)
  .dependsOn(sharedJvm)

lazy val clientJS = (project in file(clientName))
  .settings(
    commonSettings,
    name := clientName,
    version := clientVersion,
//    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
      //"org.singlespaced" %%% "scalajs-d3" % scalaJsD3Version,
      //"com.github.karasiq" %%% "scalajs-bootstrap-v4" % scalaJsBootstrapVersion,
      //"com.github.japgolly.scalajs-react" %%% "core" % "1.2.0"
    )
  ).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file(sharedName))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq("com.lihaoyi" %%% "scalatags" % scalaTagsVersion)
  )
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
//onLoad in Global := (onLoad in Global).value andThen {s: State => s"project $serverName" :: s}


/*

scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/src/main/scala/root-doc.md")

//Set the environment variable for hosts allowed in testing
fork in Test := true
envVars in Test := Map("TAP_HOSTS" -> "localhost")


//Documentation - run ;paradox;copyDocs
enablePlugins(ParadoxPlugin) //Generate documentation with Paradox
paradoxTheme := Some(builtinParadoxTheme("generic"))
paradoxProperties in Compile ++= Map(
  "github.base_url" -> s"$githubBaseUrl",
  "scaladoc.api.base_url" -> s"$scaladocApiBaseUrl"
)
//Task for copying to root level docs folder (for GitHub pages)
val copyDocsTask = TaskKey[Unit]("copyDocs","copies paradox docs to /docs directory")
copyDocsTask := {
  val docSource = new File("target/paradox/site/main")
  val apiSource = new File("target/scala-2.12/api")
  val docDest = new File("docs")
  val apiDest = new File("docs/api")
  //if(docDest.exists) IO.delete(docDest)
  IO.copyDirectory(docSource,docDest,overwrite=true,preserveLastModified=true)
  IO.copyDirectory(apiSource,apiDest,overwrite=true,preserveLastModified=true)
}


javaOptions in Universal ++= Seq(
  // -J params will be added as jvm parameters
  "-J-Xmx4g",
  "-J-Xms2g"
)
*/