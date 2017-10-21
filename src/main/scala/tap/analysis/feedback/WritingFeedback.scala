package tap.services.feedback

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout

import scala.concurrent.duration._

/*
object WritingFeedback {
  case class AwaData(meta_uid:UUID,data:AwaInputData)
}
*/

class WritingFeedback extends Actor with ActorLogging {

  implicit val executor = context.system.dispatcher
  implicit val timeout = Timeout(30 seconds)

//  val awaAggregator = context.actorSelection("/user/analyticsService/awaAggregator")
//  val dataPersistence = context.actorSelection("/user/dataPersistence")
//  val documentFeedback = context.actorSelection("/user/feedbackService/documentFeedback")
//  val paragraphFeedback = context.actorSelection("/user/feedbackService/paragraphFeedback")
//  val sentenceFeedback = context.actorSelection("/user/feedbackService/sentenceFeedback")
//  val expressionFeedback = context.actorSelection("/user/feedbackService/expressionFeedback")

  var writerLevel = 2 // 1 - weak, 2 - moderate, 3 - strong

  override def receive = {
//    case awaInput: AwaData => {
//      log.info("Received data to analyse from: {}",sender.path.toString)
//      collectFeedback(awaInput.data,awaInput.meta_uid,sender)
//    }
    case _ => sender ! "Invalid input"
  }
/*
  def collectFeedback(awaInputData: AwaInputData,meta_uid:UUID,sender:ActorRef): Unit = {
    val inputData = new InputData(awaInputData)
    log.info("AWA request for user: {} of subject: {} for doc: {} - research: {}",inputData.userRef,inputData.subjectCode,inputData.docRef,inputData.researchCode)
    //val saveResult = for(result <- ask(dataPersistence,inputData).mapTo[Future[(Boolean,UUID)]].flatMap(identity)) yield result
    //saveResult.onComplete{
    //  case Success(result) => log.info("Save result for inputData: {}",result)
    //  case Failure(e) => log.error("An error occurred saving inputData: {}",e.printStackTrace)
    //}
    val fbData = for {
      a <- ask(awaAggregator, inputData).mapTo[Future[Analytics]].flatMap(identity)
    } yield FeedbackRequestData(a,inputData,writerLevel)

    fbData.onComplete {
      case Success(fb) => {
        val fbr = fb.asInstanceOf[FeedbackRequestData].analytics
        saveAnalytics(meta_uid,fbr)
      }
      case _ => log.error("There was a problem getting the analytics ready for feedback construction")
    }

    val output = for {
      m <- Future(metaData(inputData))
      d <- ask(documentFeedback,fbData).mapTo[Future[List[AwaOutputData]]].flatMap(identity)
      p <- ask(paragraphFeedback,fbData).mapTo[Future[List[AwaOutputData]]].flatMap(identity)
      s <- ask(sentenceFeedback,fbData).mapTo[Future[List[AwaOutputData]]].flatMap(identity)
      e <- ask(expressionFeedback,fbData).mapTo[Future[List[AwaOutputData]]].flatMap(identity)
      //f <- fbData
    } yield m ++ d ++ p ++ s ++ e

    output.onComplete {
      case Success(out) => {
        saveAwaOutput(meta_uid,out)
        sender ! out
      }
      case _ => log.error("There was a problem getting the awa output")
    }

  }

  def saveAnalytics(uid:UUID,analytics:Analytics):String = {
    val saveResult = for(result <- ask(dataPersistence,(uid,analytics))) yield result
    saveResult.mapTo[Future[Boolean]].flatMap(identity).onComplete{
      case Success(result) => log.info("Save result for analytics: {}",result)
      case Failure(e) => log.error("An error occurred saving analytics: {}",e.printStackTrace)
    }
    "done"
  }

  def saveAwaOutput(uid:UUID,output:List[AwaOutputData]) = {
    val saveResult = for(result <- ask(dataPersistence,AwaOutputMsg(uid,output))) yield result
    saveResult.mapTo[Future[Boolean]].flatMap(identity).onComplete{
      case Success(result) => log.info("Save result for awaOutput: {}",result)
      case Failure(e) => log.error("An error occurred saving awaOutput: {}",e.printStackTrace)
    }
  }

  def metaData(inputData:InputData):List[AwaOutputData] = {
    List(AwaOutputData("metadataQuery","this is the metadataQuery",List("API Version: "+Config.version,"Input Timestamp: "+inputData.timestamp),0,0,0,0))
  }
*/
}

//case class FeedbackRequestData(analytics:Analytics, inputData:InputData, writerLevel:Int)
