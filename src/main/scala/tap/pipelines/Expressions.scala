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

import play.api.Logger
import tap.data.CustomTypes.{AffectExpression, EpistemicExpression, ModalExpression}
import tap.data._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import tap.analysis.affectlexicon.AffectLexiconActor._

import scala.util.{Failure, Success}
/**
  * Created by andrew@andrewresearch.net on 16/10/17.
  */
class Expressions @Inject()(@Named("affectlexicon") affectlexicon: ActorRef){

  val logger: Logger = Logger(this.getClass)

  /* Initialise AffectLexicon in an Actor */
  implicit val timeout: Timeout = 120 seconds
  val affectLexiconInitialised:Future[Boolean] = ask(affectlexicon,INIT).mapTo[Boolean]
  affectLexiconInitialised.onComplete {
    case Success(result) => logger.info(s"AffectLexicon initialised successfully: $result")
    case Failure(e) => logger.error("AffectLexicon encountered an error on startup: " + e.toString)
  }

  def affect(tokens:Vector[TapToken]):Future[Vector[AffectExpression]] = {
    ask(affectlexicon,getAffectTerms(tokens)).mapTo[Vector[AffectExpression]]
  }

  def epistemic(tokens:Vector[TapToken]):Future[Vector[EpistemicExpression]] = {
    ask(affectlexicon,getEpistemicVerbs(tokens)).mapTo[Vector[EpistemicExpression]]
  }


  def modal(tokens:Vector[TapToken]):Future[Vector[ModalExpression]] = {
    ask(affectlexicon,getModalVerbs(tokens)).mapTo[Vector[ModalExpression]]
  }
}
