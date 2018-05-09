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

package tap.io

import java.io.{File, InputStream}
import java.nio.file.{Path, Paths}

import akka.NotUsed
import akka.stream.IOResult
import akka.util.ByteString

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import tap.pipelines.materialize.PipelineContext.materializer
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by andrew@andrewresearch.net on 1/3/17.
  */
object Local {

  import akka.stream.scaladsl._

  def directorySource(directory: String):Source[Path,NotUsed] = {

    val dir = new File(getClass.getResource(directory).getPath)
    println(dir.toString)
    val files = dir.listFiles.filter(f => f.isFile && f.canRead).toList.map(f => Paths.get(f.getAbsolutePath))
    files.foreach(println(_))
    Source(files)
  }

  case class CorpusFile(name:String,contents:String)

  val fileFlow:Flow[Path,Future[CorpusFile],NotUsed] = Flow[Path].map( path =>fileSource(path).toMat(Sink.head[ByteString])(Keep.right).run().map(contents => CorpusFile(path.getFileName.toString,contents.utf8String)))


  def fileSource(path:Path):Source[ByteString,Future[IOResult]] = FileIO.fromPath(path)

 val pipeline = directorySource("/").via(fileFlow)
}
