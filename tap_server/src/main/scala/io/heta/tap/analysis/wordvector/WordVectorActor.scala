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

package io.heta.tap.analysis.wordvector

import java.io.File

import akka.actor.Actor
import com.typesafe.scalalogging.Logger
import io.heta.tap.analysis.wordvector.WordVectorActor._
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.deeplearning4j.models.word2vec.Word2Vec

import scala.collection.JavaConverters._
import scala.util.Try

/** Initialise WordVectors in an Actor  */
object WordVectorActor {
  object INIT
  case class getNearestWords(word:String, numberOfNearestWords: Int)
}

/**
  * WordVectors in an Actor
  */
class WordVectorActor extends Actor {
  val logger: Logger = Logger(this.getClass)

  val gModel = new File("models/googleNews/GoogleNews-vectors-negative300.bin.gz")
  val vec= Try(Some(WordVectorSerializer.readWord2VecModel(gModel))).getOrElse(None)

  /**
    * Receive messages that the actor can handle: Such as INIT, and gNearestWords
    *
    * @return A [[scala.PartialFunction PartialFunction]] of type [[scala.Any Any]] and Unit[[scala.Unit Unit]]
    */
  def receive: PartialFunction[Any,Unit] = {
    case INIT => sender ! init
    case gNearestWords: getNearestWords => sender ! getNearestWords(gNearestWords.word, gNearestWords.numberOfNearestWords)
    case msg:Any => logger.error(s"WordVectorActor received unknown msg: $msg")
  }

  /**
    * Initialisation
    */
  def init:Boolean = {
    vec != None
  }

  /**
    * Word nearest
    *
    * @param word the word to compare
    * @param numberOfNearestWords
    * @return
    */
  def getNearestWords(word:String, numberOfNearestWords: Int): Option[Array[String]] = {
    if (vec!= None) {
      val wordCollection = vec.get.wordsNearest(word, numberOfNearestWords)
      return Some(wordCollection.asScala.toArray)
    }
    else
      return None
  }
}