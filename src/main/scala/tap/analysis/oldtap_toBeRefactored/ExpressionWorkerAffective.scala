package tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */

import akka.actor.{Actor, ActorLogging}

class ExpressionWorkerAffective extends Actor with ActorLogging{



  override def receive = {
    //case annotations:Annotations => sender ! affective(annotations.tapAnnotations,annotations.paraIndex)
    case _ => log.error("ExpressionWorkerAffective received an unknown message!")
  }

}

