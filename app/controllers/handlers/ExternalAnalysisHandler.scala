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

package controllers.handlers

import javax.inject.Inject
import play.api.Logger
import controllers.external.athanor.AthanorClient
import io.heta.tap.data.results.StringListResult

import scala.concurrent.Future
/**
  * Created by andrew@andrewresearch.net on 19/9/17.
  */

/**
  * Handles all client's text analysising requests with Athanor Server
  *
  * @param athanorClient is a controller for client and Athanor Server
  */
class ExternalAnalysisHandler @Inject() (athanorClient: AthanorClient) extends GenericHandler {

  val logger: Logger = Logger(this.getClass)

  /**
    * Performs analysis request with Athanor
    *
    * @param text Optional version text
    * @param parameters Optional version parameters
    * @param start Long type
    * @return A [[scala.concurrent.Future Future]] with type [[StringListResult]]
    */
  def analyseWithAthanor(text:Option[String],parameters:Option[String],start:Long):Future[StringListResult] = {
    val inputText = text.getOrElse("")
    val grammar = extractParameter[String]("grammar",parameters)
    //logger.warn(s"grammar |$grammar|")
    val grammarParam = "?grammar=" + grammar.getOrElse("nogrammar")
    logger.debug(s"Creating request with parameter: $grammarParam")
    athanorClient.process(inputText,grammarParam,start)
  }

}