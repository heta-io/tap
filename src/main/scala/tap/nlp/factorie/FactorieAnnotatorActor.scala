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

import akka.actor.Actor
import cc.factorie.app.nlp._
import io.nlytx.factorie.nlp.api.DocumentBuilder
import play.api.Logger
import play.api.Logger.logger
import tap.nlp.factorie.FactorieAnnotatorActor.{INIT, MakeDocument, TestDocument}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by andrew@andrewresearch.net on 21/10/17.
  */

object FactorieAnnotatorActor {
  object INIT

  case class MakeDocument(text:String)
  case class TestDocument(text:String)

}

class FactorieAnnotatorActor extends Actor {

  val db = new DocumentBuilder

  def receive: PartialFunction[Any,Unit] = {
    case INIT => init
    case md:MakeDocument => sender ! document(md.text)
    case td:TestDocument => sender ! tokenSegment(td.text)
    case msg:Any => {
      Logger.error(s"FactorieAnnotatorActor received unkown msg: $msg")
    }
  }

  def init:Unit = {
    logger.info("Initialising Factorie")
    val futureDoc = db.process[db.Complete]("Initialising factory")
    futureDoc.onComplete {
      case Success(d) if(d.tokenCount==2) => {
        logger.info("Factorie initialised successfully!")
      }
      case Failure(e) => {
        logger.error("There was a problem initialising factorie.")
      }
      case _ => {
        logger.error("There was a problem initialising factorie.")
      }
    }
  }

  def document(text:String):Future[Document] = {
    //val doc = new Document(text)
    db.process[db.Complete](text)
    //logger.info("Annotator info: " + annotator.default.profileReport)
    //doc
  }

  def tokenSegment(text:String):Future[Document] = {
    db.process[db.TokenSegment](text)
  }


}
