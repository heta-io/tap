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

import java.time.Instant

import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.util.ByteString
import io.heta.tap.pipelines.materialize.FilePipeline.File
import io.heta.tap.pipelines.materialize.PipelineContext.materializer

import scala.concurrent.Future

object FilePipeline {
  case class FileInfo(name:String,size:Long,lastModified:Instant)
  case class File(name:String,contents:ByteString)
}

case class FilePipeline(source:Source[File,NotUsed], flow:Flow[File,File,NotUsed], sink:Sink[File,Future[Done]]) extends Pipeline {
  private val pipeline =  source.via(flow).toMat(sink)(Keep.right)
  def run: Future[Done] = pipeline.run()
}





