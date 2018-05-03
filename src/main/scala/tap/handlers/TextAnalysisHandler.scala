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

package tap.handlers

import javax.inject.Inject
import models.graphql.Fields._
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import tap.pipelines.materialize.TextPipeline
import tap.pipelines.{Annotating, Cleaning}
import tap.pipelines.AnnotatingTypes._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

/**
  * Created by andrew@andrewresearch.net on 20/2/17.
  */
class TextAnalysisHandler @Inject() (clean: Cleaning, annotate: Annotating) {

  private val pipe = annotate.Pipeline

  private def queryTime(start:Long):Int = (System.currentTimeMillis() - start).toInt

  private def extractParameter(paramName:String,parameters:Option[String]):JsValue = {
    val jsonResult = Try(Json.parse(parameters.getOrElse("{}")))
    val jparam = jsonResult.getOrElse(JsObject(Seq()))
    (jparam \ paramName).getOrElse(JsString(""))
  }

  def clean(text: Option[String], parameters: Option[String], start:Long): Future[StringResult] = {
    val inputStr = text.getOrElse("")
    val outputStr:Future[String] = extractParameter("cleanType",parameters) match {
      case JsString("visible") => TextPipeline(inputStr,clean.Pipeline.revealInvisible).run
      case JsString("minimal") => TextPipeline(inputStr,clean.Pipeline.utfMinimal).run
      case JsString("simple") => TextPipeline(inputStr,clean.Pipeline.utfSimplify).run
      case JsString("preserve") => TextPipeline(inputStr,clean.Pipeline.lengthPreserve).run
      case JsString("ascii") =>  TextPipeline(inputStr,clean.Pipeline.asciiOnly).run
      case _ => Future("")
    }
    outputStr.map(StringResult(_,querytime = queryTime(start)))
  }

  def annotations(text:Option[String],parameters:Option[String],start:Long):Future[SentencesResult] = {
    val inputStr = text.getOrElse("")
    val analysis = extractParameter("pipeType",parameters) match {
      case JsString(CLU) => TextPipeline(inputStr, annotate.build(CLU,pipe.cluSentences)).run
      case JsString(STANDARD) => TextPipeline(inputStr, annotate.build(STANDARD,pipe.sentences)).run
      case JsString(FAST) => TextPipeline(inputStr, annotate.build(FAST,pipe.sentences)).run
      case JsString(NER) => TextPipeline(inputStr, annotate.build(NER,pipe.sentences)).run
      case _ => TextPipeline(inputStr, annotate.build(FAST,pipe.sentences)).run
    }
    analysis.map(SentencesResult(_,querytime = queryTime(start)))
  }


  def expressions(text:Option[String],parameters:Option[String],start:Long):Future[ExpressionsResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.expressions)).run
      .map(ExpressionsResult(_,querytime = queryTime(start)))

  def syllables(text:Option[String],parameters:Option[String],start:Long):Future[SyllablesResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.syllables)).run
      .map(SyllablesResult(_,querytime = queryTime(start)))

  def spelling(text:Option[String],parameters:Option[String],start:Long):Future[SpellingResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.spelling)).run
      .map(SpellingResult(_,querytime = queryTime(start)))

  def vocabulary(text:Option[String],parameters:Option[String],start:Long):Future[VocabResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.vocab)).run
      .map(VocabResult(_,querytime = queryTime(start)))

  def metrics(text:Option[String],parameters:Option[String],start:Long):Future[MetricsResult]  =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.metrics)).run
      .map(MetricsResult(_,querytime = queryTime(start)))

  def posStats(text:Option[String],parameters:Option[String],start:Long):Future[PosStatsResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.posStats)).run
      .map(PosStatsResult(_,querytime = queryTime(start)))

  def reflectExpressions(text:Option[String],parameters:Option[String],start:Long):Future[ReflectExpressionsResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.reflectExpress)).run
    .map(ReflectExpressionsResult(_, querytime = queryTime(start)))

  def affectExpressions(text:Option[String],parameters:Option[String] = None,start:Long): Future[AffectExpressionsResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.affectExpress)).run
    .map(AffectExpressionsResult(_,"",queryTime(start)))



  //TODO To be implemented
  def shape(text:String):Future[StringResult]   = dummyResult(text)


  def dummyResult(text:String):Future[StringResult] = Future {
    StringResult("This features is not implemented yet")
  }


}
