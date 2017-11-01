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

package tap.pipelines

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import cc.factorie.app.nlp.Document
import org.scalatest.AsyncFlatSpec
import tap.nlp.factorie.FactorieAnnotatorActor
import tap.nlp.factorie.FactorieAnnotatorActor.TestDocument

import scala.concurrent.duration._
import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 31/10/17.
  */

class AnnotatingPipelineSpec extends AsyncFlatSpec {

//  private val system = ActorSystem("mySystem")
//  private val factorieAnnotatorTest = system.actorOf(Props[FactorieAnnotatorActor], "factorie-annotator-test")
//
//
//
//  def getDoc:Future[Document] = {
//    implicit val timeout:Timeout = 120.seconds
//    ask(factorieAnnotatorTest,TestDocument("This is a test.")).mapTo[Future[Document]].flatMap(identity(_))
//  }
//
//  "factorieAnnotatorActor" should "return a valid document" in {
//    getDoc map { doc => assert(doc.tokenCount == 5) }
//  }

}
