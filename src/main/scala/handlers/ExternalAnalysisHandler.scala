/*
 * Copyright 2016-2017 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package handlers

import javax.inject.Inject

import models.Results.StringListResult
import play.api.Logger
import tap.analysis.athanor.AthanorClient

import scala.concurrent.Future
/**
  * Created by andrew@andrewresearch.net on 19/9/17.
  */
class ExternalAnalysisHandler @Inject() (athanorClient: AthanorClient) {

  val logger: Logger = Logger(this.getClass)

  def analyseWithAthanor(text:String,grammar:Option[String]):Future[StringListResult] = {
    val parameter = "?grammar=" + grammar.getOrElse("analytic")
    logger.info(s"Creating request with parameter: $parameter")

    athanorClient.process(text,parameter)
  }

}