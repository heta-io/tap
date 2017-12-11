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

package tap.analysis.wordvector

import java.io.File
import akka.actor.{Actor, Stash}
import play.api.Logger
import tap.analysis.wordvector.WordVectorActor._
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.deeplearning4j.models.word2vec.Word2Vec

import scala.collection.JavaConverters._
import scala.util.Try

object WordVectorActor {
  object INIT
  case class getNearestWords(word:String, numberOfNearestWords: Int)
}

/*class WordVectorActor extends Actor {
  val logger: Logger = Logger(this.getClass)

  val gModel = new File("models/googleNews/GoogleNews-vectors-negative300.bin.gz")
  val vec = WordVectorSerializer.readWord2VecModel(gModel)

  def receive: PartialFunction[Any,Unit] = {
    case INIT => sender ! init
    case gNearestWords: getNearestWords => sender ! getNearestWords(gNearestWords.word, gNearestWords.numberOfNearestWords)
    case msg:Any => {
      logger.error(s"WordVectorActor received unknown msg: $msg")
    }
  }

  def init:Boolean = {
    vec.hasWord("day")
  }

  def getNearestWords(word:String, numberOfNearestWords: Int): util.Collection[String] = {
    vec.wordsNearest(word, numberOfNearestWords)
  }
}*/

/*class WordVectorActor extends Actor with Stash{
  val logger: Logger = Logger(this.getClass)

  var vec:Word2Vec = null

  def receive = uninitialized

  def uninitialized:Receive = {
    case INIT => sender ! init
    case "success" => {
      logger.info(s"WordVector Actor initialised successfully")
      context.become(success)
      unstashAll()
    }
    case "failure" => {
      logger.info(s"WordVector Actor initialised failed")
      context.become(failure)
      unstashAll()
    }
    case msg:Any => stash()/*{
      logger.error(s"Uninitialized WordVector Actor received unknown msg: $msg")
    }*/
  }

  def success:Receive = {
    case gNearestWords: getNearestWords => {
      sender ! getNearestWords(gNearestWords.word, gNearestWords.numberOfNearestWords)
    }
    case msg:Any => {
      logger.error(s"Initialized WordVector Actor received unknown msg: $msg")
    }
  }

  def failure:Receive = {
    case gNearestWords: getNearestWords => {
      val emptyArray = new Array[String](0)
      sender ! emptyArray
    }
    case msg:Any => {
      logger.error(s"Failed WordVector Actor received unknown msg: $msg")
    }
  }

  def init:Boolean = {
    val me = context.self

    val gModel = new File("models/googleNews/GoogleNews-vectors-negative300.bin.gz")
    vec = Try(WordVectorSerializer.readWord2VecModel(gModel)) getOrElse null

    if (vec != null){
      me ! "success"
      return true
    }
    else {
      me ! "failure"
      return false
    }
  }

  def getNearestWords(word:String, numberOfNearestWords: Int): Array[String] = {
    val wordCollection = vec.wordsNearest(word, numberOfNearestWords)
    wordCollection.asScala.toArray
  }
}*/

class WordVectorActor extends Actor {
  val logger: Logger = Logger(this.getClass)

  val gModel = new File("models/test/googleNews/GoogleNews-vectors-negative300.bin.gz")
  val vec= Try(Some(WordVectorSerializer.readWord2VecModel(gModel))).getOrElse(None)

  def receive: PartialFunction[Any,Unit] = {
    case INIT => sender ! init
    case gNearestWords: getNearestWords => sender ! getNearestWords(gNearestWords.word, gNearestWords.numberOfNearestWords)
    case msg:Any => logger.error(s"WordVectorActor received unknown msg: $msg")
  }

  def init:Boolean = {
    vec != None
  }

  def getNearestWords(word:String, numberOfNearestWords: Int): Option[Array[String]] = {
    if (vec!= None) {
      val wordCollection = vec.get.wordsNearest(word, numberOfNearestWords)
      return Some(wordCollection.asScala.toArray)
    }
    else
      return None
  }
}