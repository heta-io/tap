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

package models

import javax.inject.Inject

import tap.handlers.{ExternalAnalysisHandler, TextAnalysisHandler}
import models.Results._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 20/10/17.
  */

class AWAGraphqlActions @Inject() (textAnalysisHandler: TextAnalysisHandler, externalAnalysisHandler: ExternalAnalysisHandler) {

  def startTime = System.currentTimeMillis

  //  def timedQueryStringResult(text:String, analysisFunction:(String) => Future[StringResult]):Future[StringResult] = {
  //    val startTime = System.currentTimeMillis
  //    val result = analysisFunction(text)
  //    val queryTime = (System.currentTimeMillis - startTime)
  //
  //    // Copy the result object and add in the queryTimeValue then return the new result
  //    result.map(a=> a.copy(querytime = queryTime.toInt))
  //  }

  def annotations(text: String, pipetype: Option[String]): Future[SentencesResult] = textAnalysisHandler.annotations(text, pipetype, startTime)

  def vocabulary(text: String): Future[VocabResult] = textAnalysisHandler.vocabulary(text, startTime)

  def metrics(text: String): Future[MetricsResult] = textAnalysisHandler.metrics(text, startTime)

  def expressions(text: String): Future[ExpressionsResult] = textAnalysisHandler.expressions(text, startTime)

  //External Analysis Handler
  def moves(text: String, grammar: Option[String]): Future[StringListResult] = externalAnalysisHandler.analyseWithAthanor(text, grammar, startTime)

  //TODO Still to Implement

}