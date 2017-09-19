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

package handlers

import models.QueryResults._

import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 31/8/17.
  */
class QueryActions {

  def visible(text:String):Future[StringResult] = TextAnalysisHandler.visible(text)
  def clean(text:String):Future[StringResult] = TextAnalysisHandler.clean(text)
  def cleanPreserve(text:String):Future[StringResult] = TextAnalysisHandler.cleanPreserve(text)
  def cleanMinimal(text:String):Future[StringResult] = TextAnalysisHandler.cleanMinimal(text)
  def cleanAscii(text:String):Future[StringResult] = TextAnalysisHandler.cleanAscii(text)

  def sentences(text:String):Future[SentencesResult] = TextAnalysisHandler.sentences(text)
  def vocabulary(text:String):Future[VocabResult] = TextAnalysisHandler.vocabulary(text)
  def metrics(text:String):Future[MetricsResult] = TextAnalysisHandler.metrics(text)

  def moves(text:String):Future[StringListResult] = ExternalAnalysisHandler.analyseWithAthanor(text)

  //TODO Still to Implement
  def expressions(text:String):Future[StringResult] = TextAnalysisHandler.expressions(text)
  def spelling(text:String):Future[StringResult] = TextAnalysisHandler.spelling(text)
  def shape(text:String):Future[StringResult] = TextAnalysisHandler.shape(text)

}
