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

package tap.nlp.factorie

import javax.inject.Inject

import akka.actor.Actor
import cc.factorie.app.nlp._
import play.api.Logger
import play.api.Logger.logger
import tap.data.{TapSentence, TapToken}
import tap.nlp.factorie.FactorieAnnotatorActor.{INIT, MakeDocument}

/**
  * Created by andrew@andrewresearch.net on 21/10/17.
  */

object FactorieAnnotatorActor {
  object INIT
  case class MakeDocument(text:String)
}

class FactorieAnnotatorActor @Inject() (annotator: FactorieAnnotator) extends Actor {

  def receive: PartialFunction[Any,Unit] = {
    case INIT => sender ! init
    case md:MakeDocument => sender ! document(md.text)
    case msg:Any => {
      Logger.error(s"FactorieAnnotatorActor received unkown msg: $msg")
    }
  }

  //private val annotator = DocumentAnnotatorPipeline(pos.OntonotesForwardPosTagger, parse.WSJTransitionBasedParser)

  def init:Boolean = {
    logger.info("Initialising Factorie")
    document("Initialising factory").tokenCount==2
  }

  def document(text:String):Document = {
    val doc = new Document(text)
    annotator.default.process(doc)
    doc
  }



}
