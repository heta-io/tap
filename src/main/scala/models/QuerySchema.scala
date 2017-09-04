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

import handlers.QueryActions
import models.QueryResults._
import sangria.macros.derive.{Interfaces, ObjectTypeDescription, deriveObjectType}
import sangria.schema.{Argument, Field, InterfaceType, ObjectType, OptionType, Schema, StringType, fields}

/**
  * Created by andrew@andrewresearch.net on 31/8/17.
  */

object QuerySchema {


  def createSchema:Schema[QueryActions,Unit] = {

    implicit val ResultType:InterfaceType[Unit,Result] = InterfaceType("Result",
      fields[Unit, Result](Field("timestamp", StringType, resolve = _.value.timestamp)))

    implicit val StringResultType:ObjectType[Unit,StringResult] = deriveObjectType[Unit,StringResult](Interfaces(ResultType))

    implicit val TokenResultType:ObjectType[Unit,TokensResult] = deriveObjectType[Unit,TokensResult](Interfaces(ResultType))

    implicit val AnalyticsResultType:InterfaceType[Unit,AnalyticsResult] = InterfaceType("AnalyticsResult",fields[Unit,AnalyticsResult]())

    implicit val StringAnalyticsResultType:ObjectType[Unit,StringAnalyticsResult] = deriveObjectType[Unit,StringAnalyticsResult](ObjectTypeDescription("This is the analytics result"))
    implicit val TokensAnalyticsResultType:ObjectType[Unit,TokensAnalyticsResult] = deriveObjectType[Unit,TokensAnalyticsResult](ObjectTypeDescription("This is the analytics result"))

    val textArg:Argument[String] = Argument("text", StringType)

    val QueryType:ObjectType[QueryActions,Unit] = ObjectType("Query", fields[QueryActions,Unit](
      Field("visible",OptionType(StringAnalyticsResultType),
        description = Some("Returns the text showing nonstandard characters"),
        arguments = textArg :: Nil,
        resolve = c => c.ctx.visible(c arg textArg)
      ),
      Field("clean",OptionType(StringAnalyticsResultType),
        description = Some("Cleans text"),
        arguments = textArg :: Nil,
        resolve = c => c.ctx.clean(c arg textArg)
      ),
      Field("cleanPreserve",OptionType(StringAnalyticsResultType),
        description = Some("Cleans text preserving original length"),
        arguments = textArg :: Nil,
        resolve = c => c.ctx.cleanPreserve(c arg textArg)
      ),
      Field("cleanMinimal",OptionType(StringAnalyticsResultType),
        description = Some("Minmally cleans text"),
        arguments = textArg :: Nil,
        resolve = c => c.ctx.cleanMinimal(c arg textArg)
      ),
      Field("cleanAscii",OptionType(StringAnalyticsResultType),
        description = Some("Returns ascii safe cleaned text"),
        arguments = textArg :: Nil,
        resolve = c => c.ctx.cleanAscii(c arg textArg)
      ),
      Field("tokens",OptionType(TokensAnalyticsResultType),
        description = Some("Returns a list of tokens from the text"),
        arguments = textArg :: Nil,
        resolve = c => c.ctx.tokens(c arg textArg)
      )
    ))

    Schema(QueryType)
  }
}
