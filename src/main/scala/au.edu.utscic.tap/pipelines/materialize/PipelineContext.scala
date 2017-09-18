package au.edu.utscic.tap.pipelines.materialize

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

/**
  * Created by andrew@andrewresearch.net on 6/9/17.
  */

object PipelineContext {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
}
