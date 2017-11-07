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

import handlers.{ExternalAnalysisHandler, TextAnalysisHandler}
import models.Results._

import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 20/10/17.
  */

class GraphqlActions @Inject() (textAnalysisHandler: TextAnalysisHandler, externalAnalysisHandler: ExternalAnalysisHandler) {

  //Text Analysis Handler
  def visible(text:String):Future[StringResult]       = textAnalysisHandler.visible(text)
  def clean(text:String):Future[StringResult]         = textAnalysisHandler.clean(text)
  def cleanPreserve(text:String):Future[StringResult] = textAnalysisHandler.cleanPreserve(text)
  def cleanMinimal(text:String):Future[StringResult]  = textAnalysisHandler.cleanMinimal(text)
  def cleanAscii(text:String):Future[StringResult]    = textAnalysisHandler.cleanAscii(text)

  def annotations(text:String,pipetype:Option[String]):Future[SentencesResult]  = textAnalysisHandler.annotations(text,pipetype)
  def vocabulary(text:String):Future[VocabResult]     = textAnalysisHandler.vocabulary(text)
  def metrics(text:String):Future[MetricsResult]      = textAnalysisHandler.metrics(text)
  def expressions(text:String):Future[ExpressionsResult] = textAnalysisHandler.expressions(text)
  def syllables(text:String):Future[SyllablesResult]  = textAnalysisHandler.syllables(text)
  def spelling(text:String):Future[SpellingResult]    = textAnalysisHandler.spelling(text)
  def posStats(text:String):Future[PosStatsResult]    = textAnalysisHandler.posStats(text)

  //External Analysis Handler
  def moves(text:String,grammar:Option[String]):Future[StringListResult]  = externalAnalysisHandler.analyseWithAthanor(text,grammar)

  //TODO Still to Implement

  def shape(text:String):Future[StringResult] = textAnalysisHandler.shape(text)
}