// Copyright (C) 2017 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package handlers

import akka.NotUsed
import akka.stream.scaladsl.Flow
import au.edu.utscic.tap.pipelines.{Cleaning, TextPipeline}
import models.QueryResults.{AnalyticsResult, StringAnalyticsResult, StringResult}

import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 20/2/17.
  */
object TextAnalysisHandler {



  type TapPipe = Flow[String,String,NotUsed]

  val VISIBLE:TapPipe = Cleaning.Pipeline.revealInvisible
  val CLEAN:TapPipe = Cleaning.Pipeline.utfSimplify
  val CLEAN_PRESERVE:TapPipe = Cleaning.Pipeline.lengthPreserve
  val CLEAN_MINIMAL:TapPipe = Cleaning.Pipeline.utfMinimal
  val CLEAN_ASCII:TapPipe = Cleaning.Pipeline.asciiOnly

  //val SYNTAGMATIC:TapPipe = Syntagmatic.Pipeline.sectionise
  //val RHETORICAL:TapPipe = CLEAN.via(Rhetorical.Pipeline.sentenceMoves)
  //val VOCAB:TapPipe = CLEAN.via(SYNTAGMATIC).via(Vocab.pipeline))

  //      //case "complexity" => getAnalysis[AllComplexity]("complexityAggregator",msg,sender)
  ////      case "expressions" => getAnalysis[AllExpressions]("expressionAnalyser",msg,sender)
  ////      case "metrics" => getAnalysis[AllMetrics]("metricsAnalyser",msg,sender)
  ////      case "pos" => getAnalysis[AllPosStats]("posAnalyser",msg,sender)
  ////      case "spelling" => getAnalysis[AllSpelling]("spellingAnalyser",msg,sender)
  ////      case "syllables" => getAnalysis[AllSyllables]("syllableAnalyser",msg,sender)
  ////      case "vocab" => getAnalysis[AllVocab]("vocabAnalyser",msg,sender)
  ////      case "xip" => getAnalysis[DocumentXip]("xipAnalyser",msg,sender)
  ////      case "textShape" => getAnalysis[String]("textshapeAnalyser",msg,sender)

  import au.edu.utscic.tap.TapStreamContext._

  def analyse(text:String,pipeline:TapPipe):Future[StringAnalyticsResult] = TextPipeline(text,pipeline).run.map( str => StringAnalyticsResult(StringResult(str)))

}
