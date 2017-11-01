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

package tap.analysis

import java.io.InputStream

import play.api.Logger

//import org.json4s.{DefaultFormats, Formats, Serialization, jackson}
//import org.json4s.jackson

/**
  * Created by andrew@andrewresearch.net on 16/10/17.
  */
object Lexicons {

  val logger: Logger = Logger(this.getClass)

  def matchEpistemicVerbs(terms:Vector[String],useLemmas:Boolean = false):Vector[String] = terms
    .intersect(if (useLemmas) epistemicVerbLemmas else epistemicVerbTerms)

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




  def mostEmotional(threshold:Double=3.5):Vector[Affect] = allAffectTerms.filter(_.arousal> threshold).sortBy(_.valence)

  def getSubSets(ratio:Double = 0.25, threshold:Double = 3.0):(AffectLexicon,AffectLexicon,Double,Double) = {
    val me = mostEmotional(threshold)
    val valence = me.map(_.valence)
    val size = (ratio * me.size).toInt
    (me.take(size),me.takeRight(size),valence.min,valence.max)
  }

  def load(filename:String):Vector[Affect] = {
    import play.api.libs.json._
    lazy val stream : InputStream = getClass.getResourceAsStream(filename)
    lazy val src = scala.io.Source.fromInputStream( stream )
    implicit val affectReads = Json.reads[Affect]
    val jsonString: JsValue = Json.parse(src.getLines().mkString)
    Json.fromJson[List[Affect]](jsonString).getOrElse(List()).toVector
  }

  case class Affect(word:String,valence:Double,arousal:Double,dominance:Double)
}
