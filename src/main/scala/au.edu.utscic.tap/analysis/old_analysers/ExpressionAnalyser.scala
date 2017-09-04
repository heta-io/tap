package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 13/07/2016.
  */

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout

import scala.concurrent.duration._

class ExpressionAnalyser extends Actor with ActorLogging {

  //import context._
  implicit val executor = context.system.dispatcher
  implicit val timeout = Timeout (30 seconds)
//  val expressionWorkerAnnotating = context.actorOf(Props[ExpressionWorkerAnnotating],"expressionWorkerAnnotating")
//  val expressionWorkerModal = context.actorOf(Props[ExpressionWorkerModal],"expressionWorkerModal")
//  val expressionWorkerEpistemic = context.actorOf(Props[ExpressionWorkerEpistemic],"expressionWorkerEpistemic")
//  val expressionWorkerAffective = context.actorOf(Props[ExpressionWorkerAffective],"expressionWorkerAffective")

  override def preStart() = {
    log.debug("Starting ExpressionAnalyser")
    //val startup = Annotating("pre-load models")
  }
  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.error(reason, "Restarting ExpressionAnalyser due to [{}] when processing [{}]",
      reason.getMessage, message.getOrElse(""))
  }

  def receive = {
    //case text:String => sender ! expressions(text)
    //case request:ParagraphRequest => sender ! paragraphExpressions(request)
    //case request:DocumentRequest => sender ! documentExpressions(request)
    case _ => sender ! "Invalid input"
  }
/*
  def expressions(text:String):Future[AllExpressions] = {
    val paras = text.split("\n").toList
    val pef = paras.zipWithIndex.map { case (para, idx) =>
      val request = ParagraphRequest(idx + 1, para) //paragraph indexes start at 1
      paragraphExpressions(request)
    }
    for {
      pes <- Future.sequence(pef)
    filtered <- Future(pes.filterNot(_.paraExpressions.isEmpty))
      de <- documentExpressions(filtered)
    } yield AllExpressions(de, filtered)
  }

  def paragraphExpressions(request:ParagraphRequest):Future[ParagraphExpressions] = {
    log.debug("Creating ParagraphExpressions for paragraph {}",request.index)
    for {
      annotations <- ask(expressionWorkerAnnotating,request.text).mapTo[List[(TapAnnotation,Int)]]
      val aList = Annotations(annotations,request.index)
      modal <- ask(expressionWorkerModal,aList).mapTo[List[Expression]]
      epistemic <- ask(expressionWorkerEpistemic,aList).mapTo[List[Expression]]
      affective <- ask(expressionWorkerAffective,aList).mapTo[AffectExpressions]
      affectRating = affective.rating
      pes = findParaExpressions(request,modal ++ epistemic ++ affective.expressions)
    } yield ParagraphExpressions(pes,affectRating)
  }



  def findParaExpressions(request:ParagraphRequest,expressions:List[Expression]) = expressions.map { e =>
    val start = request.text.indexOf(e.expression)
    if(start == -1) {
      log.warning("Expression NOT found: "+request.text.take(50))
      ParagraphExpression(request.index,"ERROR","Expression ("+e.expression+") not found in original text. The original text could be unclean",-1,-1)
//      log.info("Trying with clean paragraph...")
//      //val start2 = paras(paraIndex).replaceAll("(\u0026\u0020)","").indexOf(sent)
//      val start2 = clean(request.text).indexOf(clean(e.expression))
//      if(start2 == -1) {
//        log.warning("Expression NOT found in clean paragraph - no more options")
//        ParagraphExpression(request.index,"ERROR","Expression ("+e.expression+") not found in original text. The original text could be unclean",-1,-1)
//      } else {
//        log.warning("Expression found in clean paragraph - may not match original text")
//        val end2 = start2 + e.expression.length
//        ParagraphExpression(request.index, e.expressionType, "WARNING: Unclean original text: " + e.expression, start2, end2)
//      }
    } else {
      val end = start + e.expression.length
      ParagraphExpression(request.index,e.expressionType,e.expression,start,end)
    }
  }

  def documentExpressions(request:DocumentRequest):Future[DocumentExpressions] = {
    //log.debug("Calculating DocumentVocab for paragraphs")
    for {
      pe <- request.paragraphs.flatMap(pas => Future(pas.map(_.expressions)))
    } yield DocumentExpressions(pe)
  }

  def documentExpressions(paras:List[ParagraphExpressions]):Future[DocumentExpressions] = Future(DocumentExpressions(paras.filterNot(_.paraExpressions.isEmpty)))
*/
}

//case class Annotations(tapAnnotations:List[(TapAnnotation,Int)],paraIndex:Int)