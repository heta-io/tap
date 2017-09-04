package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 13/07/2016.
  */

import akka.actor.{Actor, ActorLogging}

/**
  * Created by andrew@andrewresearch.net on 13/07/2016.
  */

class MetricsAnalyser extends Actor with ActorLogging {

  override def preStart() = {
    log.debug("Starting MetricsAnalyser")
  }
  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.error(reason, "Restarting MetricsAnalyser due to [{}] when processing [{}]",
      reason.getMessage, message.getOrElse(""))
  }

  def receive = {
//    case text:String => {
//      //log.info("Received text: {}",text.take(20))
//      sender ! metrics(text)
//    }
//    case text:InputData => sender ! metrics(text)
//    case request:ParagraphRequest => sender ! paragraphMetrics(request)
//    case request:DocumentRequest => sender ! documentMetrics(request)
    case _ => sender ! "Invalid input"
  }

/*
  def metrics(text:String):Future[AllMetrics] = {
    val paras = text.split("\n").toList
    metrics(InputData("","",paras))
  }
  def metrics(inputData:InputData):Future[AllMetrics] = {
    val paras = inputData.text
    val pms = paras.zipWithIndex.map { case (para, idx) =>
      // For each paragraph get it's analytics
      val request = ParagraphRequest(idx + 1, para) //paragraph indexes start at 1
      paragraphMetrics(request)
    }
    for (dm <- documentMetrics(pms)) yield AllMetrics(dm, pms)
  }

  def paragraphMetrics(request:ParagraphRequest):ParagraphMetrics = {
    //log.debug("Calculating ParagraphMetrics for paragraph {}",request.index)
    val p = request.text
    val sentenceCount = TextSplit.sentenceSplit(p).size
    val words:List[String] = TextSplit.wordSplit(p)
    val wordCount = words.size
    val uniqueWordCount = words.toSet.size
    val characterCount = p.length
    ParagraphMetrics(request.index,characterCount,wordCount,uniqueWordCount,sentenceCount)
  }

  def documentMetrics(request:DocumentRequest):Future[DocumentMetrics] = {
    //log.debug("Calculating DocumentMetrics for paragraphs")
    for {
      pm <- request.paragraphs
      m <- Future(pm.map(_.metrics))
      dm <- documentMetrics(m)
    } yield dm
  }

  def documentMetrics(paras:List[ParagraphMetrics]):Future[DocumentMetrics] = {
    val m = paras
    for {
      cc <- Future(m.map(_.characterCount).sum)
      wc <- Future(m.map(_.wordCount).sum)
      uwc <- Future(m.map(_.wordCount).toSet.sum)
      sc <- Future(m.map(_.sentenceCount).sum)
      po <- Future(calculateOffsets(m,wc))
    } yield DocumentMetrics(cc,wc,uwc,sc,po)
  }

  private def calculateOffsets(pms:List[ParagraphMetrics],wc:Int):List[Double] = offsetHelper(0.0,List(),pms.map(_.wordCount),wc)

  private def offsetHelper(offset:Double,offsetList:List[Double],paraWordCounts:List[Int],totalWordCount:Int):List[Double] = {
    if (paraWordCounts.isEmpty) offsetList
    else {
      val coverage = paraWordCounts.head.toDouble / totalWordCount
      offsetHelper(offset+coverage,offsetList:+offset,paraWordCounts.tail,totalWordCount)
    }
  }
  */

  /*
  private def cleaner(text:String):String = {
    val newText = clean(text)
    val ntl = newText.length
    val tl = text.length
    if(ntl != tl) {
      log.error("Orig: "+tl+" New: "+ntl)
      log.error("Text is unclean: "+showInvisibles(text))
      log.error(newText)
    }
    newText
  }
  */

}
