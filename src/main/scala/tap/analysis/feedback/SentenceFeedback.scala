package tap.services.feedback

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */

import akka.actor.{Actor, ActorLogging}


class SentenceFeedback extends Actor with ActorLogging {

  implicit val executor = context.system.dispatcher
  //implicit val timeout = Timeout(30 seconds)

  override def receive = {
    //case feedbackData: Future[FeedbackRequestData] => sender ! sentenceLevelFeedback(feedbackData)
    case _ => sender ! "Invalid input"
  }
/*
  def sentenceLevelFeedback(fbData:Future[FeedbackRequestData]):Future[List[AwaOutputData]] = {
    for { data <- fbData } yield sentenceXipFB(data)
  }

  def sentenceXipFB(feedbackData:FeedbackRequestData):List[AwaOutputData] = {
    val xipData = feedbackData.analytics.documentAnalytics.xip.sentences
    val startParas: List[String] = feedbackData.inputData.text
    val paras: List[String] = startParas.filterNot(p => p.trim.isEmpty)
    val removed = startParas.length - paras.length
    if (removed > 0) log.warning("Removed {} empty paragraphs", removed)
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

  private def sentenceFeedback(resp:(SentenceXip,Int),paras:List[String]):AwaOutputData = {
    //val cleaner = new TextCleaning()
    //def clean(t:String) = cleaner.clean(t)
    //def escape(t:String) = cleaner.htmlEscape(t)
    //def showInvisibles(t:String) = cleaner.showInvisibles(t,"@")
    //def codes(t:String) = t.map(cleaner.charCode(_))

    val paraIndex = resp._2-1
    val numParas = paras.length
    //log.debug("Para: "+(paraIndex+1))
    //log.debug("Number of REFLCONC: "+resp._1.REFLCONC.length)
    val sent = resp._1.REFLCONC.head
    val start = paras(paraIndex).indexOf(sent)
    if(start == -1) {
      log.error("Sentence NOT found: |"+sent+"|") //+showInvisibles(sent)+"|")
      log.debug("Looking in para "+paraIndex+" of "+numParas) //: "+showInvisibles(paras(paraIndex))+"|")
      AwaOutputData("ERROR","XIP Sentence not found in original text",List(sent),paraIndex,paraIndex,start,start)
      //log.info("Trying with clean paragraph...")
      //val start2 = paras(paraIndex).replaceAll("(\u0026\u0020)","").indexOf(sent)
//      val start2 = clean(paras(paraIndex)).indexOf(clean(escape(sent)))  //TODO Escaping the text is a hack as XIP is not returning encoded text. This can be removed when XIP encodes the text
//      if(start2 == -1) {
//        log.error("Sentence NOT found in clean paragraph - no more options")
//        AwaOutputData("ERROR","XIP Sentence not found in original text",List(showInvisibles(sent)),paraIndex,paraIndex,start,start)
//      } else {
//        log.warning("Sentence found in clean paragraph - may not match original text")
//        val end2 = start2 + sent.length
//        val tags = resp._1.REFLCONC_M.head
//        AwaOutputData("annotation", "Reflection Concepts - WARNING: Unclean paragraph", tags, resp._2, resp._2, start2, end2)
//      }
    } else {
      val end = start + sent.length
      AwaOutputData("annotation", "Reflection Concepts", resp._1.REFLCONC_M.head, resp._2, resp._2, start, end)
    }
  }
  */
}
