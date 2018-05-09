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
import play.api.Logger
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import tap.data.AffectThresholds
import tap.pipelines.materialize.TextPipeline
import tap.pipelines.{Annotating, Cleaning}
import tap.pipelines.AnnotatingTypes._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try
import scala.reflect.runtime.universe._

/**
  * Created by andrew@andrewresearch.net on 20/2/17.
  */
class TextAnalysisHandler @Inject() (clean: Cleaning, annotate: Annotating) {

  private val pipe = annotate.Pipeline

  def clean(text: Option[String], parameters: Option[String], start:Long): Future[StringResult] = {
    val inputStr = text.getOrElse("")
    val json = validJson(parameters)
    Logger.debug(s"JSON: $json")
    val cleanType = extractParameter[String]("cleanType",json)
    Logger.debug(s"STRING: $cleanType")
    val outputStr:Future[String] = cleanType match {
      case Some("visible") => TextPipeline(inputStr,clean.Pipeline.revealInvisible).run
      case Some("minimal") => TextPipeline(inputStr,clean.Pipeline.utfMinimal).run
      case Some("simple") => TextPipeline(inputStr,clean.Pipeline.utfSimplify).run
      case Some("preserve") => TextPipeline(inputStr,clean.Pipeline.lengthPreserve).run
      case Some("ascii") =>  TextPipeline(inputStr,clean.Pipeline.asciiOnly).run
      case _ => Future("")
    }
    outputStr.map(StringResult(_,querytime = queryTime(start)))
  }

  def annotations(text:Option[String],parameters:Option[String],start:Long):Future[SentencesResult] = {
    val inputStr = text.getOrElse("")
    val json = validJson(parameters)
    Logger.debug(s"JSON: $json")
    val pipeType = extractParameter[String]("pipeType",json)
    Logger.debug(s"STRING: $pipeType")
    val analysis = pipeType match {
      case Some(CLU) => TextPipeline(inputStr, annotate.build(CLU,pipe.cluSentences)).run
      case Some(STANDARD) => TextPipeline(inputStr, annotate.build(STANDARD,pipe.sentences)).run
      case Some(FAST) => TextPipeline(inputStr, annotate.build(FAST,pipe.sentences)).run
      case Some(NER) => TextPipeline(inputStr, annotate.build(NER,pipe.sentences)).run
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

  def affectExpressions(text:Option[String],parameters:Option[String] = None,start:Long): Future[AffectExpressionsResult] = {
    val thresholds = extractAffectThresholds(parameters)
    Logger.debug(s"thresholds: $thresholds")
    TextPipeline(text.getOrElse(""),annotate.build(CLU,pipe.affectExpress(thresholds))).run
      .map(AffectExpressionsResult(_,"",queryTime(start)))
  }



  private def queryTime(start:Long):Int = (System.currentTimeMillis() - start).toInt

  private def validJson(parameters:Option[String]):Option[JsValue] = parameters.flatMap(p => Try(Json.parse(p)).toOption).map(_.result.get)

  private def extractParameter[A:TypeTag](paramName:String,jsParams:Option[JsValue]):Option[Any] = jsParams.flatMap { jp =>
      val result = Try((jp \ paramName).toOption).toOption.flatten
      typeOf[A] match {
        case t if t =:= typeOf[String] => Try(result.map(_.as[String])).toOption.flatten
        case t if t =:= typeOf[Double] => Try(result.map(_.as[Double])).toOption.flatten
        case t if t =:= typeOf[Int] => Try(result.map(_.as[Int])).toOption.flatten
        case _ => None
      }
    }



  private def extractAffectThresholds(parameters:Option[String]):Option[AffectThresholds] = {
    val jsonParams = validJson(parameters)
    Logger.debug(s"PARAMS: $jsonParams")
    for {
      v <- extractParameter[Double]("valence",jsonParams)
      a <- extractParameter[Double]("arousal",jsonParams)
      d <- extractParameter[Double]("dominance",jsonParams)
    } yield AffectThresholds(v.asInstanceOf[Double],a.asInstanceOf[Double],d.asInstanceOf[Double])
  }


  //TODO To be implemented
  def shape(text:String):Future[StringResult]   = dummyResult(text)


  def dummyResult(text:String):Future[StringResult] = Future {
    StringResult("This features is not implemented yet")
  }


}
