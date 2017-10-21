package tap.services.feedback

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */

import akka.actor.{Actor, ActorLogging}


class ExpressionFeedback extends Actor with ActorLogging {

  implicit val executor = context.system.dispatcher
  //implicit val timeout = Timeout(30 seconds)

  override def receive = {
    //case feedbackData: Future[FeedbackRequestData] => sender ! expressionLevelFeedback(feedbackData)
    case _ => sender ! "Invalid input"
  }
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
}
