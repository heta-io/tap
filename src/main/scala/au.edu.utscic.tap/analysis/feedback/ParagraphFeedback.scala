package au.edu.utscic.tap.services.feedback

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */

import akka.actor.{Actor, ActorLogging}


class ParagraphFeedback extends Actor with ActorLogging {

  implicit val executor = context.system.dispatcher
  //implicit val timeout = Timeout(30 seconds)

  override def receive = {
    //case feedbackData: Future[FeedbackRequestData] => sender ! paragraphLevelFeedback(feedbackData)
    case _ => sender ! "Invalid input"
  }
/*
  def paragraphLevelFeedback(fbData:Future[FeedbackRequestData]):Future[List[AwaOutputData]] = {
    for {
      data <- fbData
      short <- Future(shortParas(data))
      expressions <- Future(filterShort(expressionFB(data),short))
      spelling <- Future(filterShort(spellingFB(data),short))
    } yield expressions ++ spelling
  }
  def shortParas(feedbackData:FeedbackRequestData):List[Int] = {
    val short = feedbackData.inputData.text.zipWithIndex.map{ case(p,i) =>
      if(p.split('.').length < 2 ) i+1 else -1}.filterNot(_ == -1)
    log.debug("Found short paragraphs: {}",short.toString)
    short
  }

  def filterShort(output:List[AwaOutputData],short:List[Int]):List[AwaOutputData] = {
    output.filterNot(o => short.contains(o.lineStart))
  }

  def expressionFB(feedbackData:FeedbackRequestData):List[AwaOutputData] = {

    (1 to feedbackData.inputData.text.length).map { i =>
      val expressionCounts =  countExpressions(feedbackData.analytics.paragraphAnalytics.apply(i-1).expressions.paraExpressions)
      log.debug("Expression counts for paragraph {}: [Critique]: {}, [Epistemic]: {}, [Emotion]: {}",i,expressionCounts.critique,expressionCounts.epistemic,expressionCounts.emotion)
      //val comment = if(expressionCounts.none) List(ExpressionText.noExpressions) else if(expressionCounts.onlyEmotion) List(ExpressionText.onlyEmotion)
      val comment = if(expressionCounts.onlyEmotion) List(ExpressionText.onlyEmotion)
      else List()
      AwaOutputData("text", "expressions", comment, i, i, 0, 0)
    }.toList.filterNot(_.fbData.isEmpty)
  }

  def countExpressions(paraExpressions:List[ParagraphExpression]):ExpressionCounts = {
    val critique = paraExpressions.count(_.expressionType.contains("CRITIQUE"))
    val epistemic = paraExpressions.count(_.expressionType.contains("EPISTEMIC"))
    val emotion = paraExpressions.count(_.expressionType.contains("EMOTION"))
    ExpressionCounts(critique,epistemic,emotion)
  }
  case class ExpressionCounts(critique:Int, epistemic:Int, emotion:Int) {
    //def none:Boolean = (critique==0 && epistemic==0 && emotion==0)
    def onlyEmotion:Boolean = (critique==0 && epistemic==0)
    //def tooMany(sentenceCount:Int):Boolean = (sentenceCount < 1.2*(context + challenge + change))
  }


  def spellingFB(feedbackData:FeedbackRequestData):List[AwaOutputData] = {
    (1 to feedbackData.inputData.text.length).map { i =>
      val parAnalytics = feedbackData.analytics.paragraphAnalytics.apply(i-1)
      val spellingData = parAnalytics.spelling.spelling
      val neCount = parAnalytics.posStats.namedEntities.size
      val spellErrors = spellingData.errorCounts.map(_._2).sum
      val totalErrors = if(neCount > spellErrors) 0 else spellErrors - neCount
      log.debug("Para Spell Count: {}, NE Count: {} Error Count: {}",spellErrors,neCount,totalErrors)
      val feedback = if (SpellText.showCounts) {
        val errors = spellingData.errorCounts.filterNot(_._2==0).map(e => e._1.replaceAll("Spelling mistake","Unknown Word") -> e._2)
        val errorString = errors.map( e => e._1+" ("+e._2+")").mkString(", ")
        val feedbackDetail = if(errorString.isEmpty) "" else "AWA found: "+errorString
        if(totalErrors==0) List(SpellText.paraNoErrors)
        else List(SpellText.paraSomeErrors) ++ List(feedbackDetail)
      } else {
        if(totalErrors<=1)  List()
        else  List(SpellText.paraSomeErrors)
      }
      val comments = if (feedbackData.writerLevel==3) spellingData.comments.distinct else List()
      AwaOutputData("text","spelling", feedback ++ comments,i,i,0,0)
    }.toList
  }
  */
}