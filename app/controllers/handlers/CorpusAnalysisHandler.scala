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

package controllers.handlers


//import au.edu.utscic.tap.data.CorpusTfIdfResults
//import au.edu.utscic.tap.io.Local
//import au.edu.utscic.tap.io.Local.CorpusFile
//import au.edu.utscic.tap.message.Exception.UnknownAnalysisType
//import au.edu.utscic.tap.pipelines._
//import io.nlytx.commons.ranking.TfIdf
//
//import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 20/2/17.
  */
object CorpusAnalysisHandler {

  //import TapStreamContext._

//  def analyse(msg: Json.CorpusAnalysis): Future[Json.Results] = {
//    TapStreamContext.log.debug("Analysing '{}' corpus: {}", msg.analysisType, msg.corpus)
//    val pipeline = msg.analysisType match {
//      case "tfidf" => {
//        val corpus:Future[Seq[CorpusFile]] = CorpusPipelineIter(Local.directorySource("/"+msg.corpus),Local.fileFlow).run
//        corpus.map { c =>
//          val contentsList = c.toList.map(_.contents)
//          val filenamesList = c.toList.map(_.name)
//          val tfidf = TfIdf.calculateNonWeighted(contentsList,true,0.02)
//          (filenamesList zip tfidf).map(e => CorpusTfIdfResults(e._1,e._2.size,0.02,false,e._2))
//        }
//      }
//      case "file" => CorpusPipeline(Local.directorySource("/"+msg.corpus),Local.fileFlow).run
//      //Local.pipeline.toMat(Sink.seq[Future[String]])(Keep.right) //.via(Local.pipeline).toMat(Sink.seq[String])(Keep.right)
//      //case "topic" => s"Analysing ${msg.corpus} for ${msg.analysisType}" //Pipeline(sourceFrom(msg.byteStr),Cleaning.pipeline.via(Parsing_OpenNLP.pipeline))
//      case _ => {
//        throw UnknownAnalysisType("Unknown analysis type")
//      }
//    }
//
//    Json.formatResults(pipeline, "Corpus Analysis Results") //.map(Future.sequence(_))
//    //Json.formatStringResults(pipeline,"Corpus Analysis Results")
//  }
}

