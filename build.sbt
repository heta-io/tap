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

name := "tap"

version := "3.1.1"

scalaVersion := "2.12.4"

organization := "au.edu.utscic"

//Scala library versions
val nlytxNlpApiV = "1.1.0"
val nlytxNlpCommonsV = "1.0.0"
val factorieNlpV = "1.0.4"
val factorieNlpModelsV = "1.0.3"

val sangriaVersion = "1.3.2"
val sangriaJsonVersion = "1.0.4"
val playJsonVersion = "2.6.7"
val twirlApiVersion = "1.3.13"

val akkaStreamVersion = "2.5.6"
val scalatestVersion = "3.0.4"
val scalatestPlayVersion = "3.1.2"

//Java library versions
val openNlpVersion = "1.8.3"
val langToolVersion = "3.9"

enablePlugins(PlayScala)
disablePlugins(PlayLayoutPlugin)
PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value
libraryDependencies += ws     //Http client
libraryDependencies += guice  //Dependency injection

val apiDependencies = Seq(
  "org.sangria-graphql" %% "sangria" % sangriaVersion,
  "com.typesafe.play" %% "play-json" % playJsonVersion,
  "com.typesafe.play" %% "twirl-api" % twirlApiVersion,
  "org.sangria-graphql" %% "sangria-play-json" % sangriaJsonVersion
)

val analyticsDependencies = Seq(
  "io.nlytx" %% "nlytx-nlp-api" % nlytxNlpApiV,
  "io.nlytx" %% "nlytx-nlp-commons" % nlytxNlpCommonsV,
  "io.nlytx" %% "factorie-nlp" % factorieNlpV,
  "io.nlytx" %% "factorie-nlp-models" % factorieNlpModelsV,
  "com.typesafe.akka" % "akka-stream_2.12" % akkaStreamVersion,
  "org.apache.opennlp" % "opennlp-tools" % openNlpVersion,
  "org.languagetool" % "language-en" % langToolVersion
)
resolvers += Resolver.bintrayRepo("nlytx", "nlytx-nlp")

val testDependencies = Seq(
  "org.scalactic" %% "scalactic" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % scalatestPlayVersion % "test",
  "com.typesafe.akka" % "akka-stream-testkit_2.12" % akkaStreamVersion
)


libraryDependencies ++= apiDependencies ++ analyticsDependencies ++ testDependencies

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

enablePlugins(JavaAppPackaging) // sbt universal:packageZipTarball
dockerExposedPorts := Seq(9000,80) // sbt docker:publishLocal
dockerRepository := Some(s"$dockerRepoURI")
defaultLinuxInstallLocation in Docker := "/opt/docker"
dockerExposedVolumes := Seq("/opt/docker/logs")
javaOptions in Universal ++= Seq(
  // -J params will be added as jvm parameters
  "-J-Xmx4g",
  "-J-Xms2g"
)
