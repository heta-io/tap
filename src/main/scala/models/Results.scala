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

import java.time.OffsetDateTime

import tap.data._
import models.Results.Implicits.ResultType
import sangria.macros.derive.{GraphQLDescription, GraphQLName, Interfaces, deriveObjectType}
import sangria.schema.{Field, IntType, InterfaceType, ObjectType, StringType, fields}

/**
  * Created by andrew@andrewresearch.net on 17/10/17.
  */
object Results {

  trait Result {
    val analytics: Any
    val timestamp: String = OffsetDateTime.now().toString
    val querytime: Int
    val message: String
  }

  case class StringResult(analytics: String, message:String = "", querytime:Int = -1) extends Result

  case class StringListResult(analytics: Vector[Vector[String]], message:String = "", querytime:Int = -1) extends Result

  case class SentencesResult(analytics: Vector[TapSentence], message:String = "", querytime:Int = -1) extends Result

  case class VocabResult(analytics: TapVocab, message:String = "", querytime:Int = -1) extends Result

  case class MetricsResult(analytics: TapMetrics, message:String = "", querytime:Int = -1) extends Result

  case class PosStatsResult(analytics: TapPosStats, message:String = "", querytime:Int = -1) extends Result

  case class ExpressionsResult(analytics: Vector[TapExpressions], message:String = "", querytime:Int = -1) extends Result

  case class SpellingResult(analytics: Vector[TapSpelling], message:String = "", querytime:Int = -1) extends Result

  @GraphQLName("syllables")
  @GraphQLDescription("Get syllable counts and averages.")
  case class SyllablesResult(analytics: Vector[TapSyllables], message:String = "", querytime:Int = -1) extends Result

  val StringResultType =  deriveObjectType[Unit,StringResult](Interfaces[Unit,StringResult](ResultType))

  object Implicits {
    implicit val ResultType:InterfaceType[Unit,Result] = InterfaceType(
      "Result", fields[Unit, Result](
        Field("timestamp", StringType, resolve = _.value.timestamp),
        Field("querytime", IntType, resolve = _.value.querytime),
        Field("message", StringType, resolve = _.value.message)
      )
    )
    implicit val TokenType:ObjectType[Unit,TapToken] = deriveObjectType[Unit,TapToken]()
    implicit val SentenceType:ObjectType[Unit,TapSentence] = deriveObjectType[Unit,TapSentence]()
    implicit val TermCountType:ObjectType[Unit,TermCount] = deriveObjectType[Unit,TermCount]()
    implicit val VocabType:ObjectType[Unit,TapVocab] = deriveObjectType[Unit,TapVocab]()
    implicit val MetricsType:ObjectType[Unit,TapMetrics] = deriveObjectType[Unit,TapMetrics]()
    implicit val TapExpressionType:ObjectType[Unit,TapExpression] = deriveObjectType[Unit,TapExpression]()
    implicit val TapExpressionsType:ObjectType[Unit,TapExpressions] = deriveObjectType[Unit,TapExpressions]()
    implicit val tapSyllablesType:ObjectType[Unit,TapSyllables] = deriveObjectType[Unit,TapSyllables]()
    implicit val TapSpellingType:ObjectType[Unit,TapSpelling] = deriveObjectType[Unit,TapSpelling]()
    implicit val TapSpellType:ObjectType[Unit,TapSpell] = deriveObjectType[Unit,TapSpell]()
    implicit val TapPosStatsType:ObjectType[Unit,TapPosStats] = deriveObjectType[Unit,TapPosStats]()
  }
}
