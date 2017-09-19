package au.edu.utscic.tap.pipelines.materialize

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Sink, Source}
import au.edu.utscic.tap.pipelines.materialize.PipelineContext.materializer

case class TextPipeline[T](inputStr: String, flow: Flow[String,T,NotUsed]) {

  val source = Source.single(inputStr)
  def run = source via flow runWith(Sink.head[T])
}