package au.edu.utscic.tap.pipelines.materialize

import akka.NotUsed
import akka.stream.scaladsl.Source

import scala.collection.immutable

/**
  * Created by andrew@andrewresearch.net on 27/2/17.
  */

trait Pipeline {
  def sourceFrom[A,B](input:immutable.Iterable[A]):Source[A,NotUsed] = Source[A](input)
}

//object PipeUtil {
//  def sourceFrom[A,B](input:immutable.Iterable[A]):Source[A,NotUsed] = Source[A](input)
//}





