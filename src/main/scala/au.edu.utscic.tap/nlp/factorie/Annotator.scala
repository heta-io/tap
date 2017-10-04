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

package au.edu.utscic.tap.nlp.factorie

import au.edu.utscic.tap.data.{TapSentence, TapToken}
import cc.factorie.app.nlp._
import play.api.Logger.logger

/**
  * Created by andrew@andrewresearch.net on 30/8/17.
  */

  object Annotator {
    private val annotator = DocumentAnnotatorPipeline(pos.OntonotesForwardPosTagger, parse.WSJTransitionBasedParser)
    logger.warn("Initialising Factorie")
    def init:Int = document("Initialising factory").tokenCount

  def document(text:String):Document = {
    val doc = new Document(text)
    annotator.process(doc)
    doc
  }

  def sections(doc:Document):List[Section] = {
    doc.sections.toList
  }

  def sentences(doc:Document):List[Sentence] = {
    doc.sentences.toList
  }

  def tapSentences(sentences:List[Sentence]):List[TapSentence] = {
    sentences.map { s =>
      val tokens = s.tokens.toList.map { t =>
        //val pt = t.parseLabel.printName
        TapToken(t.string,t.lemmaString,t.posTag.value.toString,t.parseParentIndex,0,t.parseLabel.value.toString())
      }
      TapSentence(tokens,s.start,s.end,s.length)
    }
  }

}
