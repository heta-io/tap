package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 13/07/2016.
  */

import akka.actor.{Actor, ActorLogging}

/**
  * Created by andrew@andrewresearch.net on 13/07/2016.
  */

class SyllableAnalyser extends Actor with ActorLogging {

  override def preStart() = {
    log.debug("Starting SyllableAnalyser")
  }
  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.error(reason, "Restarting SyllableAnalyser due to [{}] when processing [{}]",
      reason.getMessage, message.getOrElse(""))
  }

  def receive = {
//    case text:String => sender ! syllables(text)
//    case text:InputData => sender ! syllables(text)
//    case request:ParagraphRequest => sender ! paragraphSyllables(request)
//    case request:DocumentRequest => sender ! documentSyllables(request)
    case _ => sender ! "Invalid input"
  }
/*
  def syllables(text:String):Future[AllSyllables] = {
    val paras = text.split("\n").toList
    syllables(InputData("","",paras))
  }

  def syllables(inputData:InputData):Future[AllSyllables] = {
    val paras = inputData.text
    val pps = paras.zipWithIndex.map { case (para, idx) =>
      val request = ParagraphRequest(idx + 1, para) //paragraph indexes start at 1
      paragraphSyllables(request)
    }
    for (dp <- documentSyllables(pps)) yield AllSyllables(dp, pps)
  }

  def paragraphSyllables(request:ParagraphRequest):ParagraphSyllables = {
    //log.debug("Calculating ParagraphSyllables for paragraph {}",request.index)
    val words = TextSplit.wordSplit(request.text).map(_.toLowerCase)
    val syllables = words.map(w => countSyllables(w)).sum / words.length.toDouble
    ParagraphSyllables(request.index,syllables,words.size)
  }

  def documentSyllables(request:DocumentRequest):Future[DocumentSyllables] = {
    //log.debug("Calculating DocumentSyllables for paragraphs")
    for {
      paras <- request.paragraphs.flatMap(pas => Future(pas.map(_.syllables)))
      ps <- documentSyllables(paras)
    } yield ps
  }

  def documentSyllables(paras:List[ParagraphSyllables]):Future[DocumentSyllables] = {
    for {
      ps <- Future(paras)
      ds = ps.map(_.averageSyllables)
      totalWordCount = ps.map(_.wordCount).sum
      totalSyllables = ps.map(p => p.averageSyllables * p.wordCount).sum
      average = totalSyllables / totalWordCount
    } yield DocumentSyllables(average,ds)
  }

  private def countSyllables(word:String): Int = {
    val CLE = "([^aeiouy_]le)"
    val CVCE = "([^aeiou_]{1}[aeiouy]{1}[^aeiouy_]{1,2}e)"
    //val VVN = "([aiouCVLEN]{1,2}[ns])"
    val CVVC = "([^aeiou_][aeiou]{2}[^aeiouy_])"
    val CVC = "([^aeiou_][aeiouy][^aeiou_])"
    val CVV = "([^aeiou_][aeiou][aeiouy])"
    val VC = "([aeiou][^aeiou_])"
    val VR = "([aeiouyr]{1,2})"
    val C = "([^aeiou_])"

    word
      .replaceAll("um","_")
      .replaceAll("([aeo])r","_")
      .replaceAll(CLE,"_")
      .replaceAll(CVCE,"_")
      .replaceAll(CVVC,"_")
      .replaceAll(CVC,"_")
      .replaceAll(CVV,"_")
      .replaceAll(VC,"_")
      .replaceAll(VR,"_")
      .replaceAll(C,"").length
  }
*/
}
