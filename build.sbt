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

name := "sangria-test"

version := "0.1"

scalaVersion := "2.12.3"

enablePlugins(PlayScala)
disablePlugins(PlayLayoutPlugin)
PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value

libraryDependencies += guice

libraryDependencies ++= Seq(
  "org.sangria-graphql" %% "sangria" % "1.3.0",
  "com.typesafe.play" %% "play-json" % "2.6.0",
  "org.sangria-graphql" %% "sangria-play-json" % "1.0.3",
  "org.scalatest" %% "scalatest" % "3.2.0-SNAP9" % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % "test"
)