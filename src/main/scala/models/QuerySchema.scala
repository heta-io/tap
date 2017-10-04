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

import au.edu.utscic.tap.data._
import handlers.QueryActions
import models.QueryResults._
import sangria.macros.derive.{Interfaces, ObjectTypeDescription, deriveObjectType}
import sangria.schema.{Argument, Field, InterfaceType, ObjectType, OptionType, Schema, StringType, fields}

/**
  * Created by andrew@andrewresearch.net on 31/8/17.
  */

object QuerySchema {

  def createSchema:Schema[QueryActions,Unit] = {

    implicit val ResultType:InterfaceType[Unit,Result] = InterfaceType(
      "Result", fields[Unit, Result](Field("timestamp", StringType, resolve = _.value.timestamp)))
    implicit val StringResultType:ObjectType[Unit,StringResult] = deriveObjectType[Unit,StringResult](Interfaces(ResultType))
    implicit val StringListResultType:ObjectType[Unit,StringListResult] = deriveObjectType[Unit,StringListResult](Interfaces(ResultType))
    implicit val TokenType:ObjectType[Unit,TapToken] = deriveObjectType[Unit,TapToken]()
    implicit val SentenceType:ObjectType[Unit,TapSentence] = deriveObjectType[Unit,TapSentence]()
    implicit val SentencesResultType:ObjectType[Unit,SentencesResult] = deriveObjectType[Unit,SentencesResult](Interfaces(ResultType))
    implicit val TermCountType:ObjectType[Unit,TermCount] = deriveObjectType[Unit,TermCount]()
    implicit val VocabType:ObjectType[Unit,TapVocab] = deriveObjectType[Unit,TapVocab]()
    implicit val VocabResultType:ObjectType[Unit,VocabResult] = deriveObjectType[Unit,VocabResult](Interfaces(ResultType))
    implicit val MetricsType:ObjectType[Unit,TapMetrics] = deriveObjectType[Unit,TapMetrics]()
    implicit val MetricsResultType:ObjectType[Unit,MetricsResult] = deriveObjectType[Unit,MetricsResult](Interfaces(ResultType))

    val inputText:Argument[String] = Argument("text", StringType)

    val QueryType:ObjectType[QueryActions,Unit] = ObjectType("Query", fields[QueryActions,Unit](
      Field("visible",OptionType(StringResultType), description = Some("Returns the text showing nonstandard characters"), arguments = inputText :: Nil,
        resolve = c => c.ctx.visible(c arg inputText)),
      Field("clean",OptionType(StringResultType), description = Some("Cleans text"), arguments = inputText :: Nil,
        resolve = c => c.ctx.clean(c arg inputText)),
      Field("cleanPreserve",OptionType(StringResultType), description = Some("Cleans text preserving original length"), arguments = inputText :: Nil,
        resolve = c => c.ctx.cleanPreserve(c arg inputText)),
      Field("cleanMinimal",OptionType(StringResultType), description = Some("Minimally cleans text"), arguments = inputText :: Nil,
        resolve = c => c.ctx.cleanMinimal(c arg inputText)),
      Field("cleanAscii",OptionType(StringResultType), description = Some("Returns ascii safe cleaned text"), arguments = inputText :: Nil,
        resolve = c => c.ctx.cleanAscii(c arg inputText)),
      Field("sentences",OptionType(SentencesResultType), description = Some("Returns sentences for text"), arguments = inputText :: Nil,
        resolve = c => c.ctx.sentences(c arg inputText)),
      Field("vocabulary",OptionType(VocabResultType), description = Some("Returns vocabulary for text"), arguments = inputText :: Nil,
        resolve = c => c.ctx.vocabulary(c arg inputText)),
      Field("metrics",OptionType(MetricsResultType), description = Some("Returns metrics for text"), arguments = inputText :: Nil,
        resolve = c => c.ctx.metrics(c arg inputText)),
      Field("expression",OptionType(StringResultType), description = Some("This is a stub for a feature not implemented"), arguments = inputText :: Nil,
        resolve = c => c.ctx.expressions(c arg inputText)),
      Field("moves",OptionType(StringListResultType), description = Some("Returns a list of moves for the input text"), arguments = inputText :: Nil,
        resolve = c => c.ctx.moves(c arg inputText))
    ))

    Schema(QueryType)
  }
}
