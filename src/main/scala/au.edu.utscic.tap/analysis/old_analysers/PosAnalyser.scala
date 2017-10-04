package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 13/07/2016.
  */

import akka.actor.{Actor, ActorLogging}

class PosAnalyser extends Actor with ActorLogging {

  override def preStart() = {
    log.debug("Starting PosAnalyser")
    //val startup = Annotating("pre-load models")
  }
  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.error(reason, "Restarting PosAnalyser due to [{}] when processing [{}]",
      reason.getMessage, message.getOrElse(""))
  }

  def receive = {
//    case text:String => sender ! posStats(text)
//    case request:ParagraphRequest => sender ! paragraphPosStats(request)
//    case request:DocumentRequest => sender ! documentPosStats(request)
    case _ => sender ! "Invalid input"
  }

  /*
  def posStats(text:String):Future[AllPosStats] = {
    val paras = text.split("\n").toList
    val pps = paras.zipWithIndex.map { case (para, idx) =>
      val request = ParagraphRequest(idx + 1, para) //paragraph indexes start at 1
      paragraphPosStats(request)
    }
    for (dp <- documentPosStats(pps)) yield AllPosStats(dp, pps)
  }

  def paragraphPosStats(request:ParagraphRequest):ParagraphPosStats = {
    //log.debug("Calculating ParagraphVocab for paragraph {}",request.index)
    val posTags = Annotating(request.text)
    ParagraphPosStats(request.index,posTags.verbNounRatio,posTags.futureToPastRatio,posTags.nounsAndVerbsDistribution,posTags.namedEntities)
  }

  def documentPosStats(request:DocumentRequest):Future[DocumentPosStats] = {
    //log.debug("Calculating DocumentVocab for paragraphs")
    for {
      paras <- request.paragraphs.flatMap(pas => Future(pas.map(_.posStats)))
      pp <- documentPosStats(paras)
    } yield pp
  }

  def documentPosStats(paras:List[ParagraphPosStats]):Future[DocumentPosStats] = Future {
    val namedEntities = paras.map(_.namedEntities).flatten.toMap
     DocumentPosStats(0,0,Map(),namedEntities)
  }
*/
}

