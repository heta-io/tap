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

package au.edu.utscic.tap.nlp.factorie

import javax.inject.{Inject, Singleton}

import cc.factorie.app.nlp.{Document, DocumentAnnotatorPipeline, parse, pos}
import com.google.inject.ImplementedBy
import models.AnalyticsResult
import play.api.Logger.logger

import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 30/8/17.
  */

  object Annotation {
  private val annotator = DocumentAnnotatorPipeline(pos.OntonotesForwardPosTagger, parse.WSJTransitionBasedParser)


  logger.warn("Initialising Factorie")

  import au.edu.utscic.tap.TapStreamContext._

  def tokenise(text:String):Future[AnalyticsResult] = Future {
    val doc = new Document(text)
    annotator.process(doc)
    val results = doc.tokens.map( t =>
      t.lemmaString
    ).mkString("|")
    AnalyticsResult(results,Some("This was parsed by factorie"))
  }

}
