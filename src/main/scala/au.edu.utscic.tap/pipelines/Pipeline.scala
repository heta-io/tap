package au.edu.utscic.tap.pipelines

import java.nio.file.Path

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import au.edu.utscic.tap.TapStreamContext
import au.edu.utscic.tap.io.Local
import au.edu.utscic.tap.io.Local.CorpusFile

import scala.collection.immutable
import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 27/2/17.
  */

trait Pipeline {
  def sourceFrom[A,B](input:immutable.Iterable[A]):Source[A,NotUsed] = Source[A](input)
}

//object PipeUtil {
//  def sourceFrom[A,B](input:immutable.Iterable[A]):Source[A,NotUsed] = Source[A](input)
//}





