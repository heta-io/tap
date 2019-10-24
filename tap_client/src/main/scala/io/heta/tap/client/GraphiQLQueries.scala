/*
 * Copyright (c) 2016-2018 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 */

package io.heta.tap.client

/** GraphiQL Queries */
object GraphiQLQueries {

  lazy val allQueries:String = empty //affectExpressions + rhetoricalMoves
  lazy val allParams:String = emptyParams

  val empty =
    """
      |# See the TAP Queries page for queries that can be tested here
      |# This GUI is for experimentation only. For production, use the API
      |# through the TapCliPy python client, or directly from your language
      |# of choice.
      |#
      |# Queries take the following form:
      |
      |query QueryName($text:String,$parameters:String) {
      |   queryName(text:$text,parameters:$parameters) {
      |       message
      |       timestamp
      |       querytime
      |       analytics
      |   }
      |}
    """.stripMargin

  val emptyParams =
    """{
      |  "text": "",
      |  "parameters": "{}"
      |}
    """.stripMargin

  val aeDemoVariables =
    """{
      |  "text": "I have found this process extremely challenging. I hope that the future is more enjoyable and relaxing.",
      |  "parameters": "{\"arousal\":0,\"valence\":0,\"dominance\":0}",
      |}
    """.stripMargin

  private val affectExpressions =
    """
      |# TAP Example Queries
      |
      |query Affect($text:String,$parameters:String) {
      |  affectExpressions(text:$text,parameters:$parameters) {
      |    message
      |    timestamp
      |    querytime
      |    analytics {
      |      affect {
      |        text
      |        valence
      |        arousal
      |        dominance
      |        startIdx
      |        endIdx
      |      }
      |    }
      |  }
      |}
      |
      |
    """.stripMargin

  private val reflectExpressions =
    """
      |  query ReflectExpressions($input:String,$parameters:String) {
      |    reflectExpressions(text:$input,parameters:$parameters) {
      |      querytime
      |      message
      |      timestamp
      |      analytics {
      |        counts {
      |          wordCount
      |          avgWordLength
      |          sentenceCount
      |          avgSentenceLength
      |        }
      |        summary {
      |          metaTagSummary {
      |            knowledge
      |            experience
      |            regulation
      |            none
      |          }
      |          phraseTagSummary {
      |            outcome
      |            temporal
      |            pertains
      |            consider
      |            anticipate
      |            definite
      |            possible
      |            selfReflexive
      |            emotive
      |            selfPossessive
      |            compare
      |            manner
      |            none
      |          }
      |        }
      |        tags {
      |          sentence
      |          phrases
      |          subTags
      |          metaTags
      |        }
      |      }
      |    }
      |  }
    """.stripMargin

  private val rhetoricalMoves =
    """
      |query RhetoricalMoves($text: String,$moveParams:String) {
      |  moves(text:$text,parameters:$moveParams) {
      |      analytics
      |      message
      |      timestamp
      |      querytime
      |  }
      |}
    """.stripMargin


}
