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

name := "tap"

version := "3.0.6"

scalaVersion := "2.12.4"

organization := "au.edu.utscic"

//Scala library versions
val sangriaVersion = "1.3.0"
val playJsonVersion = "2.6.6"
val twirlApiVersion = "1.3.12"
val sangriaJsonVersion = "1.0.4"
val akkaStreamVersion = "2.5.6"
val scalatestVersion = "3.0.4"
val scalatestPlayVersion = "3.1.2"
val nlytxCommonsVersion = "0.1.1"
val factorieVersion = "1.2"
//Java library versions
val openNlpVersion = "1.8.2"
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
  "io.nlytx" %% "factorie-nlp-api" % "0.2",
  "cc.factorie.app.nlp" % "all-models" % factorieVersion,
  "com.typesafe.akka" % "akka-stream_2.12" % akkaStreamVersion,
  "org.apache.opennlp" % "opennlp-tools" % openNlpVersion,
  "org.languagetool" % "language-en" % langToolVersion
)

val generalDependencies = Seq(
  "io.nlytx" %% "commons" % nlytxCommonsVersion
)

val testDependencies = Seq(
  "org.scalactic" %% "scalactic" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % scalatestPlayVersion % "test",
  "com.typesafe.akka" % "akka-stream-testkit_2.12" % akkaStreamVersion
)


libraryDependencies ++= apiDependencies ++ analyticsDependencies ++ generalDependencies ++ testDependencies

scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/src/main/scala/root-doc.md")

resolvers += Resolver.bintrayRepo("nlytx", "nlytx_commons")

//Documentation - run ;paradox;copyDocs
enablePlugins(ParadoxPlugin) //Generate documentation with Paradox
paradoxTheme := Some(builtinParadoxTheme("generic"))
paradoxProperties in Compile ++= Map(
  "github.base_url" -> s"https://github.com/uts-cic/tap",
  "scaladoc.api.base_url" -> s"https://uts-cic.github.io/tap"
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
//scalacOptions in Paradox ++= Seq("-doc-root-content", baseDirectory.value+"/src/main/scala/root-doc.md")

//Enable this only for local builds - disabled for Travis
enablePlugins(JavaAppPackaging) // sbt universal:packageZipTarball
dockerExposedPorts := Seq(9000) // sbt docker:publishLocal

javaOptions in Universal ++= Seq(
  // -J params will be added as jvm parameters
  "-J-Xmx6g",
  "-J-Xms3g"

  // others will be added as app parameters
//  "-Dproperty=true",
//  "-port=8080",

  // you can access any build setting/task here
  //s"-version=${version.value}"
)

//Generate build info file
//Disable for travis CI
//enablePlugins(BuildInfoPlugin)
//buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
//buildInfoPackage := "org.goingok"
//buildInfoOptions += BuildInfoOption.BuildTime

resolvers += "IESL Release" at "http://dev-iesl.cs.umass.edu/nexus/content/groups/public"