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

//package tap.services.feedback

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */


/*
  def expressionLevelFeedback(fbData:Future[FeedbackRequestData]):Future[List[AwaOutputData]] = {
    for { data <- fbData } yield expressionFB(data)
  }
  def expressionFB(feedbackData:FeedbackRequestData):List[AwaOutputData] = {
    val paraExpressions:List[ParagraphExpressions] = feedbackData.analytics.documentAnalytics.expressions.paraExpressions
    paraExpressions.map { expressions =>
      if (!expressions.paraExpressions.isEmpty) {
        val paraIdx = expressions.paraExpressions.head.paraIndex
        val expressionOutput = expressions.paraExpressions
          .map(pe => AwaOutputData("highlight","Reflection Expressions - "+pe.expression,List(pe.expressionType),pe.paraIndex,pe.paraIndex,pe.startChar,pe.endChar))
        //TODO Temporary comment only on strong rating
        if(expressions.paraRating.contains("strong")) {
          val ac = AwaOutputData("text","Affect",List(affectComment(expressions.paraRating)),paraIdx,paraIdx,0,0)
          expressionOutput :+ ac
        } else expressionOutput
      }  else List()
    }.filterNot(_.isEmpty).flatten.distinct
  }

  def affectComment(rating:String):String = rating match {
    case "weak" => "" //ExpressionText.weakAffect
    case "strong" => ExpressionText.moderateAffect
    case "moderate" => ExpressionText.default
    case _ => ""
  }

  */

