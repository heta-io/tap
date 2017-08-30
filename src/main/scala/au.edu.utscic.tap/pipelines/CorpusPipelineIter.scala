package au.edu.utscic.tap.pipelines

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */

import java.nio.file.Path

import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import au.edu.utscic.tap.TapStreamContext
import au.edu.utscic.tap.io.Local.CorpusFile

import scala.concurrent.Future

case class CorpusPipelineIter[A,B](source:Source[Path,A],flow:Flow[Path,Future[CorpusFile],B]) extends Pipeline {
  import TapStreamContext._
  val sink = Sink.seq[Future[CorpusFile]]
  val pipeline =  source.via(flow).toMat(sink)(Keep.right)
  def run = pipeline.run().map(Future.sequence(_)).flatten
}
