package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */

import akka.actor.{Actor, ActorLogging}

class ExpressionWorkerAnnotating extends Actor with ActorLogging{

  //val annotating = new Spelling

  override def receive = {
    case text:String => {
      log.debug("Getting CoreNLP Annotations...")
      //sender ! Annotating(text).tapAnnotations.zipWithIndex
    }
  }


}
