package tap.services.feedback

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */

/*
class WritingFeedbackDebug extends Actor with ActorLogging {

  implicit val executor = context.system.dispatcher
  implicit val timeout = Timeout(30 seconds)
  val awaDebugAggregator = context.system.actorOf(Props[AwaDebugAggregator],"awaDebugAggregator")

  override def receive = {
    case awaInput: AwaInputData => sender ! collectFeedback(awaInput)
    case _ => sender ! "Invalid input"
  }

  def collectFeedback(awaInputData: AwaInputData): Future[AwaOutputDataDebug] = {
    val inputData = new InputData(awaInputData)
    val awaOutput = for {
      ad <- ask(awaDebugAggregator,inputData).mapTo[Future[AnalyticsDebug]].flatMap(identity)
      a = Analytics(ad.documentAnalytics,ad.paragraphAnalytics)
      x = ad.rawXip
      m <- Future(metaData(inputData))
      d <- Future(documentLevelFeedback(a,inputData))
      p <- Future(paragraphLevelFeedback(a,inputData))
      s <- Future(sentenceLevelFeedback(a,inputData))
      e <- Future(expressionFeedback(a,inputData))
    } yield (m ++ d ++ p ++ s ++ e,x)

    for {
      ao <- awaOutput
    } yield AwaOutputDataDebug(awaInputData,ao._2,ao._1)

  }

  def metaData(inputData:InputData):List[AwaOutputData] = {
    List(AwaOutputData("metadataQuery","this is the metadataQuery",List("API Version: "+Config.version,"Input Timestamp: "+inputData.timestamp),0,0,0,0))
  }

  def documentLevelFeedback(analytics:Analytics,inputData:InputData):List[AwaOutputData] = {
    val spellCount = analytics.documentAnalytics.spelling.spelling.map(_.spelling.errorCounts.map(_._2).sum).sum
    val parasCount = inputData.text.mkString(" ").split(" ").length
    val errorPercent = spellCount.toDouble / parasCount
    //log.debug("Error percent: "+errorPercent)
    val spellComment = if(errorPercent<0.005) {
      SpellText.docVeryFewErrors
    } else if(errorPercent<0.03) {
      SpellText.docSomeErrors(spellCount)
    } else if(errorPercent<0.1) {
      SpellText.docQuiteAlotErrors(spellCount)
    } else {
      SpellText.docTooManyErrors(spellCount)

    }
    List(AwaOutputData("text","spelling",List(spellComment),0,0,0,0))
  }

  def paragraphLevelFeedback(analytics:Analytics,inputData:InputData):List[AwaOutputData] = {
    (1 to inputData.text.length).map { i =>
      val spellingData = analytics.documentAnalytics.spelling.spelling.apply(i-1).spelling
      // Too advanced so leaving out: val comments = spellingData.comments.distinct
      val errors = spellingData.errorCounts.filterNot(_._2==0).map(e => e._1.replaceAll("Spelling mistake","Unknown Word") -> e._2)
      val errorString = errors.map( e => e._1+" ("+e._2+")").mkString(", ")
      val feedbackDetail = if(errorString.isEmpty) "" else "AWA found: "+errorString
      val totalErrors = spellingData.errorCounts.map(_._2).sum
      val feedback = if(totalErrors==0) {
        List("There doesn't appear to be any spelling errors in this paragraph - Well done.")
      } else {
        List("This paragraph appears to have "+ totalErrors +" spelling mistakes") ++
          List(feedbackDetail)
      }
      AwaOutputData("text","spelling", feedback,i,i,0,0)
    }.toList
  }

  def sentenceLevelFeedback(analytics:Analytics,inputData:InputData):List[AwaOutputData] = {
    val xipData = analytics.documentAnalytics.xip.sentences
    val startParas: List[String] = inputData.text
    val paras: List[String] = startParas.filterNot(p => p.trim.isEmpty)
    val removed = startParas.length - paras.length
    log.warning("Removed {} empty paragraphs", removed)
    var i = 0
    val indexedResponses = xipData.map { resp =>
      if (resp.AWAPARA) i = i + 1
      (resp, i)
    }
    indexedResponses
      .filterNot(_._1.REFLCONC.isEmpty)
      .filterNot(_._1.REFLCONC_M.isEmpty)
      .map(sentenceFeedback(_, paras))
  }

  def expressionFeedback(analytics:Analytics,inputData:InputData):List[AwaOutputData] = {
    val paraExpressions:List[ParagraphExpressions] = analytics.documentAnalytics.expressions.paraExpressions
    paraExpressions.map { expressions =>
      expressions.paraExpressions
        .map(pe => AwaOutputData("highlight","Reflection Expressions"+pe.expression,List(pe.expressionType),pe.paraIndex,pe.paraIndex,pe.startChar,pe.endChar))
    }.filterNot(_.isEmpty).flatten
  }


  private def sentenceFeedback(resp:(SentenceXip,Int),paras:List[String]):AwaOutputData = {
    val cleaner = new TextCleaning()
    def clean(t:String) = cleaner.clean(t)
    def showInvisibles(t:String) = cleaner.showInvisibles(t,"@")
    def codes(t:String) = t.map(cleaner.charCode(_))

    val paraIndex = resp._2-1
    val numParas = paras.length
    //log.debug("Para: "+(paraIndex+1))
    //log.debug("Number of REFLCONC: "+resp._1.REFLCONC.length)
    val sent = resp._1.REFLCONC.head
    val start = paras(paraIndex).indexOf(sent)
    if(start == -1) {
      log.error("Sentence NOT found: |"+showInvisibles(sent)+"|")
      log.debug("Looking in para "+paraIndex+" of "+numParas) //: "+showInvisibles(paras(paraIndex))+"|")
      log.info("Trying with clean paragraph...")
      //val start2 = paras(paraIndex).replaceAll("(\u0026\u0020)","").indexOf(sent)
      val start2 = clean(paras(paraIndex)).indexOf(clean(sent))
      if(start2 == -1) {
        log.error("Sentence NOT found in clean paragraph - no more options")
        AwaOutputData("ERROR","XIP Sentence not found in original text",List(showInvisibles(sent)),paraIndex,paraIndex,start,start)
      } else {
        log.warning("Sentence found in clean paragraph - may not match original text")
        val end2 = start2 + sent.length
        val tags = resp._1.REFLCONC_M.head
        AwaOutputData("annotation", "Reflection Concepts - WARNING: Unclean paragraph", tags, resp._2, resp._2, start2, end2)
      }
    } else {
      val end = start + sent.length
      AwaOutputData("annotation", "Reflection Concepts", resp._1.REFLCONC_M.head, resp._2, resp._2, start, end)
    }
  }

}
*/

/*
case class FeedbackBuilder(fbList:List[Future[AwaOutputData]] = List[Future[AwaOutputData]]()) {
  def add(fb:Future[AwaOutputData]) = FeedbackBuilder(fbList.::(fb))
  def add(fb:List[Future[AwaOutputData]]) = FeedbackBuilder(fbList ++ fb)
  //def toJsonString = "[" + this.completedList.map(_.toJsonString).mkString(",") + "]"
  private def futureList:Future[List[AwaOutputData]] = Future.sequence(fbList)
  def completedList:List[AwaOutputData] = Await.result(futureList,Duration(10, SECONDS))
}
*/