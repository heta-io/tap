package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 13/07/2016.
  */

import akka.actor.{Actor, ActorLogging, Props}
import akka.util.Timeout

import scala.concurrent.duration._

class SpellingAnalyser extends Actor with ActorLogging {

  implicit val executor = context.system.dispatcher
  implicit val timeout = Timeout (30 seconds)
  val spellingWorker = context.actorOf(Props[SpellingWorker],"spellingWorker")

  override def preStart() = {
    log.debug("Starting SpellingAnalyser")
  }
  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.error(reason, "Restarting SpellingAnalyser due to [{}] when processing [{}]",
      reason.getMessage, message.getOrElse(""))
  }

  def receive = {
//    case text:String => sender ! spelling(text)
//    case request:ParagraphRequest => sender ! paragraphSpelling(request)
//    case request:DocumentRequest => sender ! documentSpelling(request)
    //case inputData:InputData => sender ! spellcheck(inputData)
    case _ => sender ! "Invalid input"
  }
/*
  def spelling(text:String):Future[AllSpelling] = {
    val paras = text.split("\n").toList
    val ps = paras.zipWithIndex.map { case (para, idx) =>
      val request = ParagraphRequest(idx + 1, para) //paragraph indexes start at 1
      paragraphSpelling(request)
    }
    for {
      pss <- Future.sequence(ps)
      ds <- documentSpelling(pss)
    } yield AllSpelling(ds, pss)
  }

  def paragraphSpelling(request:ParagraphRequest):Future[ParagraphSpelling] = {
    //log.debug("Calculating ParagraphVocab for paragraph {}",request.index)
    for {
      sd <- ask(spellingWorker,request.text).mapTo[SpellingData]
    } yield ParagraphSpelling(request.index,sd)
  }

  def documentSpelling(request:DocumentRequest):Future[DocumentSpelling] = {
    //log.debug("Calculating DocumentVocab for paragraphs")
    for {
      ps <- request.paragraphs.flatMap(pas => Future(pas.map(_.spelling)))
    } yield DocumentSpelling(ps)
  }

  def documentSpelling(paras:List[ParagraphSpelling]):Future[DocumentSpelling] = {
    Future(DocumentSpelling(paras))
  }
*/
}

