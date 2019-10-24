/*
 * Copyright (c) 2016-2018 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 */

package io.heta.tap.pipelines.materialize

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */

import java.nio.file.Path

import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import io.heta.tap.io.Local.CorpusFile
import io.heta.tap.pipelines.materialize.PipelineContext.materializer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  *
  * @param source set of stream processing steps that has one open output
  * @param flow set of stream processing steps that has one open input and one open output
  * @tparam A
  * @tparam B
  */
case class CorpusPipelineIter[A,B](source:Source[Path,A],flow:Flow[Path,Future[CorpusFile],B]) extends Pipeline {
  val sink = Sink.seq[Future[CorpusFile]]
  val pipeline =  source.via(flow).toMat(sink)(Keep.right)
  def run = pipeline.run().map(Future.sequence(_)).flatten
}
