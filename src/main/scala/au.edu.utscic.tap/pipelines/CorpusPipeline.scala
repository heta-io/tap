package au.edu.utscic.tap.pipelines

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */

import java.nio.file.Path

import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import au.edu.utscic.tap.TapStreamContext
import au.edu.utscic.tap.io.Local

import scala.concurrent.Future

case class CorpusPipeline[A,B](source:Source[Path,A],flow:Flow[Path,Future[Local.CorpusFile],B]) extends Pipeline {
  import TapStreamContext._
  val sink = Sink.seq[Future[Local.CorpusFile]].mapMaterializedValue(_.map(Future.sequence(_)).flatten)
  val pipeline =  source.via(flow).toMat(sink)(Keep.right)
  def run = pipeline.run()
}

//case class CorpusTextPipeline[A](source:Source[String,NotUsed],flow:Flow[String,List[Map[String,Double]],NotUsed],singleOutput:Boolean)  {
//  import TapStreamContext._
//  val sink = if(singleOutput) Sink.head[List[Map[String,Double]]] else Sink.seq[List[Map[String,Double]]]
//  def run = source.via(flow).toMat(sink)(Keep.right).run()
//}






