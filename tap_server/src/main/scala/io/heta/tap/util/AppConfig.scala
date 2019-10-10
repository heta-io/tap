/*
 * Copyright (c) 2016-2018 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 */
package io.heta.tap.util

import scala.util.Try
import com.typesafe.config.ConfigFactory

/**
  * Appconfig holds aws configuration information
  */
class AppConfig{

  def getAthanorURL: Option[String] = AppConfig.parseConfig("external.servers","athanor")

  def getAwsAccessKey: Option[String] = AppConfig.parseConfig("external.aws","accessKey")

  def getAwsAccessPassword: Option[String] = AppConfig.parseConfig("external.aws","password")

  def getAwsRegion: Option[String] = AppConfig.parseConfig("external.aws","region")

}
object AppConfig {
  private lazy val conf = ConfigFactory.load("application.conf")
  private def parseConfig(configPath: String, configKey: String)= Try(conf.getConfig(configPath).getString(configKey)).toOption
}
