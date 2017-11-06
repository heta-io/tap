/*
 * Copyright 2016-2017 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package tap.services.feedback

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */



/*
object WritingFeedback {
  case class AwaData(meta_uid:UUID,data:AwaInputData)
}
*/


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


//case class FeedbackRequestData(analytics:Analytics, inputData:InputData, writerLevel:Int)
