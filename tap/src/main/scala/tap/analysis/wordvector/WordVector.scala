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

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import play.api.Logger
import tap.analysis.wordvector.WordVectorActor.{INIT, getNearestWords}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class WordVector @Inject()(@Named("wordvector") wordvector: ActorRef){
  val logger: Logger = Logger(this.getClass)

  /* Initialise WordVectors in an Actor */
  implicit val timeout: Timeout = 360 seconds
  val wordVectorInitialised:Future[Boolean] = ask(wordvector,INIT).mapTo[Boolean]
  wordVectorInitialised.onComplete {
    case Success(result) => logger.info(s"WordVector initialised successfully: $result")
    case Failure(e) => logger.error("WordVector encountered an error on startup: " + e.toString)
  }

  def nearestWords(word:String, numberOfNearestWords: Int): Future[Option[Array[String]]] = {
    ask(wordvector,getNearestWords(word, numberOfNearestWords)).mapTo[Option[Array[String]]]
  }
}
