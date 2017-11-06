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

import handlers.{ExternalAnalysisHandler, TextAnalysisHandler}
import models.Results._
import models.Results.Implicits._
import sangria.macros.derive._
import sangria.schema.{Argument, Field, ObjectType, OptionInputType, Schema, StringType, fields}

import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 31/8/17.
  */

class GraphqlSchema {

  val inputText:Argument[String] = Argument("text", StringType)
  val moveGrammar:Argument[Option[String]] = Argument("grammar",OptionInputType(StringType))
  val pipetype:Argument[Option[String]] = Argument("pipetype",OptionInputType(StringType))

  val allFields = fields[GraphqlActions,Unit](
    Field("visible", StringResultType,
      Some("Returns the text showing nonstandard characters"),
      arguments = inputText :: Nil, resolve = c => c.ctx.visible(c arg inputText)),
    Field("clean",StringResultType,
      Some("Cleans text"),
      arguments = inputText :: Nil, resolve = c => c.ctx.clean(c arg inputText)),
    Field("cleanPreserve",StringResultType,
      Some("Cleans text preserving original length"),
      arguments = inputText :: Nil, resolve = c => c.ctx.cleanPreserve(c arg inputText)),
    Field("cleanMinimal",StringResultType,
      Some("Minimally cleans text"),
      arguments = inputText :: Nil, resolve = c => c.ctx.cleanMinimal(c arg inputText)),
    Field("cleanAscii",StringResultType,
      Some("Returns ascii safe cleaned text"),
      arguments = inputText :: Nil, resolve = c => c.ctx.cleanAscii(c arg inputText)),

    Field("annotations", deriveObjectType[Unit,SentencesResult](Interfaces[Unit,SentencesResult](ResultType)),
      Some("Returns sentences for text"),
      arguments = inputText :: pipetype :: Nil, resolve = c => c.ctx.annotations(c arg inputText,c arg pipetype)),
    Field("vocabulary",deriveObjectType[Unit,VocabResult](Interfaces[Unit,VocabResult](ResultType)),
      description = Some("Returns vocabulary for text"),
      arguments = inputText :: Nil, resolve = c => c.ctx.vocabulary(c arg inputText)),
    Field("metrics",deriveObjectType[Unit,MetricsResult](Interfaces[Unit,MetricsResult](ResultType)),
      Some("Returns metrics for text"),
      arguments = inputText :: Nil, resolve = c => c.ctx.metrics(c arg inputText)),
    Field("expressions",deriveObjectType[Unit,ExpressionsResult](Interfaces[Unit,ExpressionsResult](ResultType)),
      description = Some("Returns expressions for text"),
      arguments = inputText :: Nil, resolve = c => c.ctx.expressions(c arg inputText)),
    Field("syllables", deriveObjectType[Unit,SyllablesResult](Interfaces[Unit,SyllablesResult](ResultType)),
      Some("Counts syllables in words and calculates averages for sentences"),
      arguments = inputText :: Nil, resolve = c => c.ctx.syllables(c.arg(inputText))),
    Field("spelling", deriveObjectType[Unit,SpellingResult](Interfaces[Unit,SpellingResult](ResultType)),
      Some("Returns spelling errors and suggestions for each sentence"),
      arguments = inputText :: Nil, resolve = c => c.ctx.spelling(c.arg(inputText))),
    Field("posStats",deriveObjectType[Unit,PosStatsResult](Interfaces[Unit,PosStatsResult](ResultType)),
      Some("Returns posStats for text"),
      arguments = inputText :: Nil, resolve = c => c.ctx.posStats(c arg inputText)),

    Field("moves",deriveObjectType[Unit,StringListResult](Interfaces[Unit,StringListResult](ResultType)),
      description = Some("Returns a list of moves for the input text"),
      arguments = inputText :: moveGrammar :: Nil, resolve = c => c.ctx.moves(c arg inputText,c arg moveGrammar))
  )

  def create:Schema[GraphqlActions,Unit] = Schema(ObjectType("Query",allFields))


}
