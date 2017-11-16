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
import tap.analysis.Lexicons
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

  def epistemic(tokens:Vector[TapToken]):Future[Vector[EpistemicExpression]] = Future {
    //Get the indexes of any epistemic verbs
    val epIdx = tokens.filter( t => Lexicons.epistemicVerbLemmas.contains(t.lemma)).map(_.idx)
    //Get the indexes of any personal pronouns
    val prpIdx = tokens.filter( t => t.postag.contains("PRP")).map(_.idx)
    //For each verb, check if there is pronoun index prior within 4 steps
    val pairs = epIdx.map(ei => (prpIdx.find(pi => (ei - pi) > 0 && (ei - pi) < 5),ei))
    pairs.map(p => TapExpression(tokens.slice(p._1.getOrElse(p._2),p._2+1).map(_.term).mkString(" "), p._1.getOrElse(p._2), p._2))
  }


  def modal(tokens:Vector[TapToken]):Future[Vector[ModalExpression]] = Future {
    //Get the indexes of any modals
    val modIdx = tokens.filter( t => t.postag.contains("MD")).map(_.idx)
    //Get the indexes of any personal pronouns
    val prpIdx = tokens.filter( t => t.postag.contains("PRP")).map(_.idx)
    //For each verb, check if there is pronoun index prior within 4 steps
    val pairs = modIdx.map(mi => (prpIdx.find(pi => (mi - pi) > 0 && (mi - pi) < 4),mi))
    pairs.map(p => TapExpression(tokens.slice(p._1.getOrElse(p._2),p._2+1).map(_.term).mkString(" "), p._1.getOrElse(p._2), p._2))
    /*
  def modal(annotations:List[(TapAnnotation,Int)],paraIndex:Int):List[Expression] = {
    val modals = annotations.filter(_._1.POS.contentEquals("MD")).filter(_._1.word.contains("ould"))
    val modalExpressions = modals.map { m =>
      val modalIdx = m._2
      val start = findPosIndex(annotations,"PRP",modalIdx,5,false)
      val end = findPosIndex(annotations,"VB",modalIdx,3,true)
      if (start!= -1 && end != -1) {
        val expression = annotations.filter((start to end) contains _._2)
        Some(Expression("CRITIQUE",expression.map(_._1.word).mkString(" "),start,end))
      } else None
    }
    modalExpressions.flatten
  }
  def findPosIndex(annotations:List[(TapAnnotation,Int)],posStr:String,start:Int,max:Int,forward:Boolean=true):Int = {
    val range = if(forward) {
      val end = if((start+max) < annotations.length) start + max else annotations.length -1
      (start to end)
    } else {
      val end = if((start-max) >= 0) start - max else 0
      (end to start)
    }
    val filtered = annotations.filter(range contains _._2).filter { case(a,i) => a.POS.startsWith(posStr) || a.word.contains(".")}
    if (filtered.isEmpty) -1 else if(forward) filtered.head._2 else filtered.reverse.head._2
  }
*/
    //Vector()
  }
}
