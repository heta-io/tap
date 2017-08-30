package au.edu.utscic.tap.pipelines

import akka.NotUsed
import akka.stream.scaladsl.Flow

/**
  * Created by andrew@andrewresearch.net on 29/6/17.
  */
object Rhetorical {

  object Pipeline {
    val sentenceMoves:Flow[String,String,NotUsed] = athanor
  }

  val athanor:Flow[String,String,NotUsed] = Flow[String].map {s =>
   "Not Implemented"
  }
}
