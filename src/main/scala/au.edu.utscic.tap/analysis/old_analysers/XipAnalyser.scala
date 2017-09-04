package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 13/07/2016.
  */

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout

import scala.concurrent.duration._

class XipAnalyser extends Actor with ActorLogging {
  implicit val timeout = Timeout(30 seconds)

  lazy val xipClient = context.actorSelection("/user/externalsService/xipClient")

  override def preStart() = {
    log.debug("Starting XipAnalyser")
  }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.error(reason, "Restarting XipAnalyser due to [{}] when processing [{}]",
      reason.getMessage, message.getOrElse(""))
  }

  def receive = {
    //case text:String => sender ! xip(text)
    //case request:ParagraphRequest => sender ! paragraphXip(request)
    //case request:DocumentRequest => sender ! documentXip(request)
    //case inputData: InputData => sender ! analyseWithXip(inputData)
    case _ => sender ! "Invalid input"
  }

  /*
  def xip(text:String):Future[DocumentXip] = {
    val paras = text.split("\n").toList
    analyseWithXip(InputData("","",paras))
  }

  private def analyseWithXip(inputData:InputData):Future[DocumentXip] = {
    for {
      xipJson <- ask(xipClient,inputData).mapTo[String]
      decodedJson <- Future(xmlDecode(xipJson))
      xip <- Future(xipResponsesFromJson(decodedJson))
    } yield xip.getOrElse(DocumentXip(List()))
  }

  private def xipResponsesFromJson(xipJson:String):Option[DocumentXip] = {
    xipFromJson(xipJson) match {
      case Success(data) => Some(DocumentXip(data))
      case Failure(ex) => {
        log.error("Unable to retrieve XIPResponse from json string: "+xipJson.take(100))
        log.debug("Error message: "+ex)
        None
      }
    }
  }

  private def xmlDecode(s:String):String = {
    val decoded = s.replaceAllLiterally("&lt;","<")
      .replaceAllLiterally("&apos;","'")
      .replaceAllLiterally("&gt;",">")
      .replaceAllLiterally("&amp;","&")
    log.debug("decoded text: "+decoded)
    decoded
  }

  implicit val serialformats = Serialization.formats(NoTypeHints)

  private def xipFromJson(jsonStr:String):Try[List[SentenceXip]] = Try {read[List[SentenceXip]](jsonStr)}

*/
}
