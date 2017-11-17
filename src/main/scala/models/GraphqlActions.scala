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
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 20/10/17.
  */

class GraphqlActions @Inject() (textAnalysisHandler: TextAnalysisHandler, externalAnalysisHandler: ExternalAnalysisHandler) {
  //wrap function

  def timedQueryStringResult(text:String, analysisFunction:(String) => Future[StringResult]):Future[StringResult] = {
    val startTime = System.currentTimeMillis.toInt
    val result = analysisFunction(text)
    val queryTime = (System.currentTimeMillis - startTime).toInt

    // Copy the result object and add in the queryTimeValue then return the new result
    result.map(a=> a.copy(querytime = queryTime))
  }

  val visibleFunction = (text: String)=>textAnalysisHandler.visible(text)
  def visible(text:String):Future[StringResult] = timedQueryStringResult(text, visibleFunction)

  val cleanFunction = (text: String)=>textAnalysisHandler.clean(text)
  def clean(text:String):Future[StringResult]         = timedQueryStringResult(text, cleanFunction)

  val cleanPreserveFunction = (text: String)=>textAnalysisHandler.cleanPreserve(text)
  def cleanPreserve(text:String):Future[StringResult] = timedQueryStringResult(text, cleanPreserveFunction)

  val cleanMinimalFunction = (text: String)=>textAnalysisHandler.cleanMinimal(text)
  def cleanMinimal(text:String):Future[StringResult]  = timedQueryStringResult(text, cleanMinimalFunction)

  val cleanAsciiFunction = (text: String)=>textAnalysisHandler.cleanAscii(text)
  def cleanAscii(text:String):Future[StringResult]    = timedQueryStringResult(text, cleanAsciiFunction)

  def annotations(text:String,pipetype:Option[String]):Future[SentencesResult]  = {
    val startTime = System.currentTimeMillis.toInt
    val result = textAnalysisHandler.annotations(text,pipetype)
    val queryTime = (System.currentTimeMillis - startTime).toInt

    // Copy the result object and add in the queryTimeValue then return the new result
    result.map(a=> a.copy(querytime = queryTime))
  }
  def vocabulary(text:String):Future[VocabResult]     = {
    val startTime = System.currentTimeMillis.toInt
    val result = textAnalysisHandler.vocabulary(text)
    val queryTime = (System.currentTimeMillis - startTime).toInt

    // Copy the result object and add in the queryTimeValue then return the new result
    result.map(a=> a.copy(querytime = queryTime))
  }
  def metrics(text:String):Future[MetricsResult]      = {
    val startTime = System.currentTimeMillis.toInt
    val result = textAnalysisHandler.metrics(text)
    val queryTime = (System.currentTimeMillis - startTime).toInt

    // Copy the result object and add in the queryTimeValue then return the new result
    result.map(a=> a.copy(querytime = queryTime))
  }
  def expressions(text:String):Future[ExpressionsResult] = {
    val startTime = System.currentTimeMillis.toInt
    val result = textAnalysisHandler.expressions(text)
    val queryTime = (System.currentTimeMillis - startTime).toInt

    // Copy the result object and add in the queryTimeValue then return the new result
    result.map(a=> a.copy(querytime = queryTime))
  }
  def syllables(text:String):Future[SyllablesResult]  = {
    val startTime = System.currentTimeMillis.toInt
    val result = textAnalysisHandler.syllables(text)
    val queryTime = (System.currentTimeMillis - startTime).toInt

    // Copy the result object and add in the queryTimeValue then return the new result
    result.map(a=> a.copy(querytime = queryTime))
  }
  def spelling(text:String):Future[SpellingResult]    = {
    val startTime = System.currentTimeMillis.toInt
    val result = textAnalysisHandler.spelling(text)
    val queryTime = (System.currentTimeMillis - startTime).toInt

    // Copy the result object and add in the queryTimeValue then return the new result
    result.map(a=> a.copy(querytime = queryTime))
  }
  def posStats(text:String):Future[PosStatsResult]    = {
    val startTime = System.currentTimeMillis.toInt
    val result = textAnalysisHandler.posStats(text)
    val queryTime = (System.currentTimeMillis - startTime).toInt

    // Copy the result object and add in the queryTimeValue then return the new result
    result.map(a=> a.copy(querytime = queryTime))
  }

  //External Analysis Handler
  def moves(text:String,grammar:Option[String]):Future[StringListResult]  = {
    val startTime = System.currentTimeMillis.toInt
    val result = externalAnalysisHandler.analyseWithAthanor(text,grammar)
    val queryTime = (System.currentTimeMillis - startTime).toInt

    // Copy the result object and add in the queryTimeValue then return the new result
    result.map(a=> a.copy(querytime = queryTime))
  }

  //TODO Still to Implement

  def shape(text:String):Future[StringResult] = {
    val startTime = System.currentTimeMillis.toInt
    val result = textAnalysisHandler.shape(text)
    val queryTime = (System.currentTimeMillis - startTime).toInt

    // Copy the result object and add in the queryTimeValue then return the new result
    result.map(a=> a.copy(querytime = queryTime))
  }
}