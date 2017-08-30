package au.edu.utscic.tap

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer

/**
  * Created by andrew@andrewresearch.net on 20/2/17.
  */

object TapStreamContext {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  val log = Logging(system.eventStream,"~")
}

