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

import models.Results._ // scalastyle:ignore
import tap.pipelines.materialize.TextPipeline
import tap.pipelines.{Annotating, Cleaning}
import tap.pipelines.AnnotatingTypes.{DEFAULT,validPipeType}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 20/2/17.
  */
class TextAnalysisHandler @Inject() (clean: Cleaning, annotate: Annotating) {

  private val pipe = annotate.Pipeline

  /* Cleaning Pipeline */
  def visible(text:String):Future[StringResult]       = TextPipeline(text,clean.Pipeline.revealInvisible).run.map(StringResult(_))
  def clean(text:String):Future[StringResult]         = TextPipeline(text,clean.Pipeline.utfSimplify).run.map(StringResult(_))
  def cleanPreserve(text:String):Future[StringResult] = TextPipeline(text,clean.Pipeline.lengthPreserve).run.map(StringResult(_))
  def cleanMinimal(text:String):Future[StringResult]  = TextPipeline(text,clean.Pipeline.utfMinimal).run.map(StringResult(_))
  def cleanAscii(text:String):Future[StringResult]    = TextPipeline(text,clean.Pipeline.asciiOnly).run.map(StringResult(_))

  /* Annotating Pipeline */
  def annotations(text:String,pipetype:Option[String]):Future[SentencesResult] = {
    TextPipeline(text, annotate.build(validPipeType(pipetype),pipe.sentences)).run.map(SentencesResult(_))
  }
  // DEFAULT pipetypes don't require parsing or NER so can use the FAST (DEFAULT) option
  def expressions(text:String):Future[ExpressionsResult]  = TextPipeline(text,annotate.build(DEFAULT,pipe.expressions)).run.map(ExpressionsResult(_))
  def syllables(text:String):Future[SyllablesResult]      = TextPipeline(text,annotate.build(DEFAULT,pipe.syllables)).run.map(SyllablesResult(_))
  def spelling(text:String):Future[SpellingResult]        = TextPipeline(text,annotate.build(DEFAULT,pipe.spelling)).run.map(SpellingResult(_))
  def vocabulary(text:String):Future[VocabResult]         = TextPipeline(text,annotate.build(DEFAULT,pipe.vocab)).run.map(VocabResult(_))
  def metrics(text:String):Future[MetricsResult]          = TextPipeline(text,annotate.build(DEFAULT,pipe.metrics)).run.map(MetricsResult(_))
  def posStats(text:String):Future[PosStatsResult]        = TextPipeline(text,annotate.build(DEFAULT,pipe.posStats)).run.map(PosStatsResult(_))


  //TODO To be implemented
  def shape(text:String):Future[StringResult]   = dummyResult(text)


  def dummyResult(text:String):Future[StringResult] = Future {
    StringResult("This features is not implemented yet")
  }


}
