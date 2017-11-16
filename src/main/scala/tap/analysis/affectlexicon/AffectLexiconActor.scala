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

package tap.analysis.affectlexicon

import java.io.InputStream

import akka.actor.Actor
import play.api.Logger
import tap.analysis.affectlexicon.AffectLexiconActor._
import tap.data.CustomTypes.{AffectExpression, EpistemicExpression, ModalExpression}
import tap.data.{TapExpression, TapToken}

/**
  * Created by quanie on 13/11/17.
  */

object AffectLexiconActor {
  object INIT
  case class matchEpistemicVerbs(terms:Vector[String],useLemmas:Boolean = false)
  case class getAffectTerms(tokens:Vector[TapToken])
  case class getEpistemicVerbs(tokens:Vector[TapToken])
  case class getModalVerbs(tokens:Vector[TapToken])
}

class AffectLexiconActor extends Actor {
  val logger: Logger = Logger(this.getClass)

  type Lexicon = Vector[String]
  type AffectLexicon = Vector[Affect]

  val epistemicVerbTerms:Lexicon = Vector("think","thought","believe","believed","guess","guessed","suppose","supposed",
    "sure","certain","confident","learnt","learned","imagine","imagined","wonder","wondered","consider","considered",
    "realise","realised","realize","realized","understand","understood","assume","assumed","admit")
  val epistemicVerbLemmas:Lexicon = Vector("think","believe","guess","suppose","sure","certain","confident",
    "learnt","learn","imagine","wonder","consider","realise","realize","understand","assume","admit")

  val allAffectTerms:AffectLexicon = load("/affect-lexicon.json")
  logger.info(s"Affect lexicon loaded with ${allAffectTerms.size} words.")
  private val subsets = getSubSets()
  val mostNegativeTerms:Lexicon = subsets._1.map(_.word)
  val mostPositiveTerms:Lexicon = subsets._2.map(_.word)
  val minValence:Double = subsets._3
  val maxValence:Double = subsets._4


  def receive: PartialFunction[Any,Unit] = {
    case INIT => sender ! init
    case mEV:matchEpistemicVerbs => sender ! matchEV(mEV.terms,mEV.useLemmas)
    case gAffect: getAffectTerms => sender ! getAffectTerms(gAffect.tokens)
    case gEV: getEpistemicVerbs => sender ! getEpistemicVerbs(gEV.tokens)
    case gMV: getModalVerbs => sender ! getModalVerbs(gMV.tokens)
    case msg:Any => {
      logger.error(s"AffectLexiconActor received unknown msg: $msg")
    }
  }

  def init:Boolean = {
    allAffectTerms.size > 0
  }

  def matchEV(terms:Vector[String],useLemmas:Boolean = false):Vector[String] = terms
    .intersect(if (useLemmas) epistemicVerbLemmas else epistemicVerbTerms)

  private def getPositive(terms: Vector[String]): Vector[TapExpression] = {
    val pos = terms.filter(l => mostPositiveTerms.contains(l))
    pos.map(w => TapExpression(w,-1,-1))
  }

  private def getNegative(terms: Vector[String]): Vector[TapExpression] = {
    val neg = terms.filter(l => mostNegativeTerms.contains(l))
    neg.map(w => TapExpression(w,-1,-1))
  }

  def getAffectTerms(tokens:Vector[TapToken]):Vector[AffectExpression] = {
    val terms = tokens.filterNot(_.isPunctuation).map(_.term.toLowerCase)

    val posWords = getPositive(terms)
    val negWords = getNegative(terms)

    posWords ++ negWords
  }

  def getEpistemicVerbs(tokens:Vector[TapToken]):Vector[EpistemicExpression] = {
    //Get the indexes of any epistemic verbs
    val epIdx = tokens.filter( t => epistemicVerbLemmas.contains(t.lemma)).map(_.idx)
    //Get the indexes of any personal pronouns
    val prpIdx = tokens.filter( t => t.postag.contains("PRP")).map(_.idx)
    //For each verb, check if there is pronoun index prior within 4 steps
    val pairs = epIdx.map(ei => (prpIdx.find(pi => (ei - pi) > 0 && (ei - pi) < 5),ei))
    pairs.map(p => TapExpression(tokens.slice(p._1.getOrElse(p._2),p._2+1).map(_.term).mkString(" "), p._1.getOrElse(p._2), p._2))
  }

  def getModalVerbs(tokens:Vector[TapToken]):Vector[ModalExpression] = {
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

  def load(filename:String):Vector[Affect] = {
    import play.api.libs.json._
    lazy val stream : InputStream = getClass.getResourceAsStream(filename)
    lazy val src = scala.io.Source.fromInputStream( stream )
    implicit val affectReads = Json.reads[Affect]
    val jsonString: JsValue = Json.parse(src.getLines().mkString)
    Json.fromJson[List[Affect]](jsonString).getOrElse(List()).toVector
  }

  def mostEmotional(threshold:Double=3.5):Vector[Affect] = allAffectTerms.filter(_.arousal> threshold).sortBy(_.valence)

  def getSubSets(ratio:Double = 0.25, threshold:Double = 3.0):(AffectLexicon,AffectLexicon,Double,Double) = {
    val me = mostEmotional(threshold)
    val valence = me.map(_.valence)
    val size = (ratio * me.size).toInt
    (me.take(size),me.takeRight(size),valence.min,valence.max)
  }

  case class Affect(word:String,valence:Double,arousal:Double,dominance:Double)
}
