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

package io.heta.tap.analysis.clu

import akka.actor.Actor
import com.typesafe.scalalogging.Logger
import io.heta.tap.analysis.clu.CluAnnotatorActor.{AnnotateRequest, INIT}
import org.clulab.processors.Document
import org.clulab.processors.clu.CluProcessor

/**
  * This is an Annotator Actor, but what is annotator???
  */

object CluAnnotatorActor {
  object INIT
  sealed trait Request
  case class AnnotateRequest(text:String) extends Request
}

class CluAnnotatorActor extends Actor {

  val logger: Logger = Logger(this.getClass)

  val processor = new CluProcessor()

  def receive: PartialFunction[Any,Unit] = {
    case INIT => sender ! init
    case annotate: AnnotateRequest => sender ! createAnnotatedDoc(annotate)
    case msg:Any => logger.error(s"CluAnnotator received unkown msg: ${msg.toString}") // scalastyle:ignore
  }

  def init: Boolean = {
    logger.warn("Initialising CluProcessor")
    val text = """CluProcessor is starting up!"""
    val aDoc = processor.mkDocument(text)
    processor.annotate(aDoc)
    val result:Boolean = aDoc.sentences.length==1
    if (result) {
      logger.info(s"Successfully initialised CluProcessor")
    } else {
      logger.error("Unable to initialise Cluprocessor:")
    }
    result
  }

  def createAnnotatedDoc(annotate:AnnotateRequest):Document = {
    logger.warn("In the annotator, creating the document")
    val doc = processor.mkDocument(annotate.text)
    processor.annotate(doc)
  }
}
