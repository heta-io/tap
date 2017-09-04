package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */

import akka.actor.{Actor, ActorLogging}

class SpellingWorker extends Actor with ActorLogging{

  //val speller = new Spelling

  override def receive = {
//    case text:String => {
//      //log.debug("Checking spelling")
//      sender ! speller.check(text)
//    }
    case _ => sender ! "nothing"
  }


}
