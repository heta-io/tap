/*
 * Copyright 2016-2017 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tap.pipelines.materialize

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */

import java.nio.file.Path

import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import tap.io.Local
import tap.pipelines.materialize.PipelineContext.materializer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class CorpusPipeline[A,B](source:Source[Path,A],flow:Flow[Path,Future[Local.CorpusFile],B]) extends Pipeline {
  val sink = Sink.seq[Future[Local.CorpusFile]].mapMaterializedValue(_.map(Future.sequence(_)).flatten)
  val pipeline =  source.via(flow).toMat(sink)(Keep.right)
  def run = pipeline.run()
}

//case class CorpusTextPipeline[A](source:Source[String,NotUsed],flow:Flow[String,List[Map[String,Double]],NotUsed],singleOutput:Boolean)  {
//  import TapStreamContext._
//  val sink = if(singleOutput) Sink.head[List[Map[String,Double]]] else Sink.seq[List[Map[String,Double]]]
//  def run = source.via(flow).toMat(sink)(Keep.right).run()
//}






