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

package models

import javax.inject.Inject

import au.edu.utscic.tap.nlp.factorie.{Annotation}
import handlers.TextAnalysisHandler
import sangria.macros.derive.{ObjectTypeDescription, deriveObjectType}
import sangria.schema.{Argument, Field, ObjectType, OptionType, Schema, StringType, fields}

import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 29/8/17.
  */

object TapSpecification {

  class TapActions {
    import TextAnalysisHandler._
    def visible(text:String):Future[AnalyticsResult] = analyse(text,VISIBLE)
    def clean(text:String):Future[AnalyticsResult] = analyse(text,CLEAN)
    def cleanPreserve(text:String):Future[AnalyticsResult] = analyse(text,CLEAN_PRESERVE)
    def cleanMinimal(text:String):Future[AnalyticsResult] = analyse(text,CLEAN_MINIMAL)
    def cleanAscii(text:String):Future[AnalyticsResult] = analyse(text,CLEAN_ASCII)
    def tokens(text:String):Future[AnalyticsResult] = Annotation.tokenise(text)
  }

  val qStr = Argument("text", StringType)

  implicit val AnalyticsResultType:ObjectType[Unit,AnalyticsResult] = deriveObjectType[Unit,AnalyticsResult](ObjectTypeDescription("This is the analytics result"))

  val QueryType = ObjectType("Query", fields[TapActions,Unit](
    Field("visible",OptionType(AnalyticsResultType),
      description = Some("Returns the text showing nonstandard characters"),
      arguments = qStr :: Nil,
      resolve = c => c.ctx.visible(c arg qStr)
    ),
    Field("clean",OptionType(AnalyticsResultType),
      description = Some("Cleans text"),
      arguments = qStr :: Nil,
      resolve = c => c.ctx.clean(c arg qStr)
    ),
    Field("cleanPreserve",OptionType(AnalyticsResultType),
      description = Some("Cleans text preserving original length"),
      arguments = qStr :: Nil,
      resolve = c => c.ctx.cleanPreserve(c arg qStr)
    ),
    Field("cleanMinimal",OptionType(AnalyticsResultType),
      description = Some("Minmally cleans text"),
      arguments = qStr :: Nil,
      resolve = c => c.ctx.cleanMinimal(c arg qStr)
    ),
    Field("cleanAscii",OptionType(AnalyticsResultType),
      description = Some("Returns ascii safe cleaned text"),
      arguments = qStr :: Nil,
      resolve = c => c.ctx.cleanAscii(c arg qStr)
    ),
    Field("tokens",OptionType(AnalyticsResultType),
      description = Some("Returns a list of tokens from the text"),
      arguments = qStr :: Nil,
      resolve = c => c.ctx.tokens(c arg qStr)
    )
  ))

  val schema = Schema(QueryType)
  val actions = new TapActions
}
