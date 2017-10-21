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

import tap.pipelines.materialize.TextPipeline
import tap.pipelines.{Cleaning, Annotating}
import models.Results._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by andrew@andrewresearch.net on 20/2/17.
  */
class TextAnalysisHandler @Inject() (cleaning: Cleaning, annotating: Annotating) {

  /* Cleaning Pipeline */
  def visible(text:String):Future[StringResult]       = TextPipeline(text,cleaning.Pipeline.revealInvisible).run.map(StringResult(_))
  def clean(text:String):Future[StringResult]         = TextPipeline(text,cleaning.Pipeline.utfSimplify).run.map(StringResult(_))
  def cleanPreserve(text:String):Future[StringResult] = TextPipeline(text,cleaning.Pipeline.lengthPreserve).run.map(StringResult(_))
  def cleanMinimal(text:String):Future[StringResult]  = TextPipeline(text,cleaning.Pipeline.utfMinimal).run.map(StringResult(_))
  def cleanAscii(text:String):Future[StringResult]    = TextPipeline(text,cleaning.Pipeline.asciiOnly).run.map(StringResult(_))

  /* Annotating Pipeline */
  def sentences(text:String):Future[SentencesResult]      = TextPipeline(text,annotating.Pipeline.sentences).run.map(SentencesResult(_))
  def expressions(text:String):Future[ExpressionsResult]  = TextPipeline(text,annotating.Pipeline.expressions).run.map(ExpressionsResult(_))
  def syllables(text:String):Future[SyllablesResult]      = TextPipeline(text,annotating.Pipeline.syllables).run.map(SyllablesResult(_))
  def spelling(text:String):Future[SpellingResult]        = TextPipeline(text,annotating.Pipeline.spelling).run.map(SpellingResult(_))
  def vocabulary(text:String):Future[VocabResult]         = TextPipeline(text,annotating.Pipeline.vocab).run.map(VocabResult(_))
  def metrics(text:String):Future[MetricsResult]          = TextPipeline(text,annotating.Pipeline.metrics).run.map(MetricsResult(_))


  //TODO To be implemented
  def shape(text:String):Future[StringResult]   = dummyResult(text)


  def dummyResult(text:String):Future[StringResult] = Future {
    StringResult("This features is not implemented yet")
  }


}
