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
import tap.analysis.Lexicons.Lexicon
import tap.analysis.affectlexicon.AffectLexiconActor._
import tap.data.CustomTypes.AffectExpression
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

  type AffectLexicon = Vector[Affect]

  val allAffectTerms:AffectLexicon = load("/affect-lexicon.json")
  logger.info(s"Affect lexicon loaded with ${allAffectTerms.size} words.")
  private val subsets = getSubSets()
  val mostNegativeTerms:Lexicon = subsets._1.map(_.word)
  val mostPositiveTerms:Lexicon = subsets._2.map(_.word)
  val minValence:Double = subsets._3
  val maxValence:Double = subsets._4


  def receive: PartialFunction[Any,Unit] = {
    case INIT => sender ! init
    case gAffect: getAffectTerms => sender ! getAffectTerms(gAffect.tokens)
    case msg:Any => {
      logger.error(s"AffectLexiconActor received unknown msg: $msg")
    }
  }

  def init:Boolean = {
    allAffectTerms.size > 0
  }

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
