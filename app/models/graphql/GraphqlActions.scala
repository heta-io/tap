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

package models.graphql

import controllers.handlers.{BatchAnalysisHandler, ExternalAnalysisHandler, TextAnalysisHandler}
import io.heta.tap.data.results._
import javax.inject.Inject
import models.graphql.Fields._

import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 20/10/17.
  */

/**
  * Represents Graphql specific data and business logic.
  *
  * @param textAnalysisHandler
  * @param externalAnalysisHandler
  * @param batchAnalysisHandler
  */

class GraphqlActions @Inject() (textAnalysisHandler: TextAnalysisHandler, externalAnalysisHandler: ExternalAnalysisHandler, batchAnalysisHandler: BatchAnalysisHandler) {

  def startTime = System.currentTimeMillis

  def clean(text: Option[String], parameters: Option[String]): Future[StringResult] = textAnalysisHandler.clean(text, parameters, startTime)

  def annotations(text: Option[String], parameters: Option[String]): Future[SentencesResult] = textAnalysisHandler.annotations(text, parameters, startTime)

  def vocabulary(text: Option[String], parameters: Option[String]): Future[VocabularyResult] = textAnalysisHandler.vocabulary(text, parameters, startTime)

  def metrics(text: Option[String], parameters: Option[String]): Future[MetricsResult] = textAnalysisHandler.metrics(text, parameters, startTime)

  def expressions(text: Option[String], parameters: Option[String]): Future[ExpressionsResult] = textAnalysisHandler.expressions(text, parameters, startTime)

  def syllables(text: Option[String], parameters: Option[String]): Future[SyllablesResult] = textAnalysisHandler.syllables(text, parameters, startTime)

  def spelling(text: Option[String], parameters: Option[String]): Future[SpellingResult] = textAnalysisHandler.spelling(text, parameters, startTime)

  def posStats(text: Option[String], parameters: Option[String]): Future[PosStatsResult] = textAnalysisHandler.posStats(text, parameters, startTime)

  def reflectExpressions(text: Option[String], parameters: Option[String]) :Future[ReflectExpressionsResult] = textAnalysisHandler.reflectExpressions(text, parameters, startTime)

  def affectExpressions(text:Option[String], parameters: Option[String]): Future[AffectExpressionsResult] = textAnalysisHandler.affectExpressions(text,parameters, startTime)

  //External Analysis Handler
  def moves(text:Option[String], parameters: Option[String]): Future[StringListResult] = externalAnalysisHandler.analyseWithAthanor(text, parameters, startTime)

  //Batch Analysis
  def batch(parameters: Option[String]): Future[BatchResult] = batchAnalysisHandler.analyse(parameters, startTime)

  //TODO Still to Implement

  def shape(text: String): Future[StringResult] = textAnalysisHandler.shape(text)

}