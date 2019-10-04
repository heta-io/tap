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

package io.heta.tap.analysis.affectlexicon

import java.io.InputStream

import akka.actor.Actor
import com.typesafe.scalalogging.Logger
import io.heta.tap.analysis.Lexicons.Lexicon
import io.heta.tap.analysis.affectlexicon.AffectLexicon._
import io.heta.tap.data.doc.expression.affect.AffectExpression
import io.heta.tap.data.doc.Token

/**
  * Created by quanie on 13/11/17.
  */

//object AffectLexicon {
//  object INIT
//  case class matchEpistemicVerbs(terms:Vector[String],useLemmas:Boolean = false)
//  case class getAffectTerms(tokens:Vector[TapToken])
//  case class matchAffectTerms(tokens:Vector[TapToken])
//  case class getEpistemicVerbs(tokens:Vector[TapToken])
//  case class getModalVerbs(tokens:Vector[TapToken])
//}

/**
  * Performs the analysis which shows if an input terms are positive or negative
  */

object AffectLexicon {
  val logger: Logger = Logger(this.getClass)

  type AffectLexicon = Vector[Affect]

  val allAffectTerms:AffectLexicon = load("/affect-lexicon.json")
  logger.info(s"Affect lexicon loaded with ${allAffectTerms.size} words.")
  private val subsets = getSubSets()
  val mostNegativeTerms:Lexicon = subsets._1.map(_.word)
  val mostPositiveTerms:Lexicon = subsets._2.map(_.word)
  val minValence:Double = subsets._3
  val maxValence:Double = subsets._4


//  def receive: PartialFunction[Any,Unit] = {
//    case INIT => sender ! init
//    case gAffect: getAffectTerms => sender ! getAffectTerms(gAffect.tokens)
//    case matchAffect: matchAffectTerms => sender !getAllMatchingTerms(matchAffect.tokens)
//    case msg:Any => {
//      logger.error(s"AffectLexiconActor received unknown msg: $msg")
//    }
//  }

  /**
    * Initialize
    *
    * @return with Boolean expression
    */
  def init:Boolean = {
    allAffectTerms.size > 0
  }

  /**
    * A GET request operation using a bucket URI lists information about the objects in the bucket.
    *
    * @param tokens
    * @return A vector containing [AffectExpression]
    */
  def getAllMatchingTerms(tokens:Vector[Token]):Vector[AffectExpression] = {
    val terms = tokens.map(_.lemma)
    val affectTerms = allAffectTerms.filter(a => terms.contains(a.word)).map(aff => aff.word -> aff).toMap
    tokens.map { t =>
      val affect = affectTerms.getOrElse(t.lemma,Affect(t.lemma,0,0,0))
      AffectExpression(t.term,t.idx,t.idx,affect.valence,affect.arousal,affect.dominance)
    }
  }

  /**
    * Checks if the terms are positive.
    *
    * @param terms Word or expression
    * @return A vector containing [AffectExpression]
    */
  private def getPositive(terms: Vector[String]): Vector[AffectExpression] = {
    val pos = terms.filter(l => mostPositiveTerms.contains(l))
    pos.map(w => AffectExpression(w,-1,-1))
  }

  /**
    * Checks if the terms are negative.
    *
    * @param terms Word or expression
    * @return A vector containing [AffectExpression]
    */
  private def getNegative(terms: Vector[String]): Vector[AffectExpression] = {
    val neg = terms.filter(l => mostNegativeTerms.contains(l))
    neg.map(w => AffectExpression(w,-1,-1))
  }

  /**
    * Check if the terms are positive or negative.
    *
    * @param tokens
    * @return
    */
  def getAffectTerms(tokens:Vector[Token]):Vector[AffectExpression] = {
    val terms = tokens.filterNot(_.isPunctuation).map(_.term.toLowerCase)

    val posWords = getPositive(terms)
    val negWords = getNegative(terms)

    posWords ++ negWords
  }

  /**
    * Load a file
    *
    * @param filename
    * @return
    */
  def load(filename:String):Vector[Affect] = {
    import play.api.libs.json._
    lazy val stream : InputStream = getClass.getResourceAsStream(filename)
    lazy val src = scala.io.Source.fromInputStream( stream )
    implicit val affectReads = Json.reads[Affect]
    val jsonString: JsValue = Json.parse(src.getLines().mkString)
    Json.fromJson[List[Affect]](jsonString).getOrElse(List()).toVector
  }

  /**
    * TODO
    *
    * @param threshold the magnitude that must be exceeded for a certain result
    * @return
    */
  def mostEmotional(threshold:Double=3.5):Vector[Affect] = allAffectTerms.filter(_.arousal> threshold).sortBy(_.valence)

  /**
    * TODO
    *
    * @param ratio
    * @param threshold the magnitude that must be exceeded for a certain result
    * @return
    */
  def getSubSets(ratio:Double = 0.25, threshold:Double = 3.0):(AffectLexicon,AffectLexicon,Double,Double) = {
    val me = mostEmotional(threshold)
    val valence = me.map(_.valence)
    val size = (ratio * me.size).toInt
    (me.take(size),me.takeRight(size),valence.min,valence.max)
  }

  case class Affect(word:String,valence:Double,arousal:Double,dominance:Double)
}
