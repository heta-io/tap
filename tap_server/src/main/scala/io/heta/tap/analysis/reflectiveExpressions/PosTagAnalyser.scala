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

package io.heta.tap.analysis.reflectiveExpressions


import io.heta.tap.data.doc.expression.reflect.{CodedSentence, SentencePhrase}

import scala.collection.mutable.ArrayBuffer

/**
 * Analyses Sentences, creates phraseTags and metaTags
 * Created by Andrew Gibson on 15/07/15.
 */

object PosTagAnalyser {

  def analyse(sentenceTags:Seq[Seq[String]],sentenceWords:Seq[Seq[String]]): Seq[CodedSentence] = {
    //For each sentence number (idx) analyse the sequence of tags
    sentenceTags.zipWithIndex.map { case (tags, idx) =>
      //Analyse the tags against each phraseTag pattern
      val sentencePhrases = new ArrayBuffer[SentencePhrase]()
      PhraseTag.posPattern.foreach { case (tagList, phraseTags) =>
        //First get the positions
        val patternType = tagList._1
        val positions = findPositionsForPattern(tagList,tags)
        val phrases = this.findPhrases(sentenceWords.apply(idx),positions)
        if(phrases.length!=positions.length) System.err.println("Phrases and Positions don't match")
        if(phrases.nonEmpty) sentencePhrases ++= this.filterSentencePhrase(patternType,phraseTags,phrases,positions)
      }
      val phraseTags = sentencePhrases.map(_.phraseType)
      val subTags = PhraseTag.subTags(phraseTags)
      val metaTags = PhraseTag.metaTags(subTags)
      val selfRatio = PhraseTag.selfRatio(phraseTags)
      val othersRatio = PhraseTag.othersRatio(phraseTags)
      CodedSentence(idx,
        sentenceWords.apply(idx).mkString(" "),
        metaTags.toVector, subTags.toVector, phraseTags.toVector,
        selfRatio,othersRatio,sentencePhrases.toVector)
    }
  }

  //def getCodedSentences:Seq[CodedSentence] = this.codedSentences


  private def findPositionsForPattern(tagList:(String,List[String]),tags:Seq[String]):Seq[(Int,Int)] = {
    val patternType = tagList._1
    val pattern = tagList._2
    if (patternType.contentEquals("startRepeat")) this.findStartRepeatPatterns(tags, pattern)
    else if (patternType.contentEquals("startEnd")) this.findStartEndPatterns(tags, pattern)
    else Seq()
  }

  //Given the start and end positions extract the words as a phrase
  private def findPhrases(textWords:Seq[String],positions:Seq[(Int,Int)]): Seq[String] = {
    val phrases: ArrayBuffer[String] = ArrayBuffer()
    positions.foreach { case (start, end) =>
      if (start > -1 && (end - start) > 0) {
        phrases += textWords.splitAt(start)._2.splitAt(end - start + 1)._1.mkString(" ").toLowerCase
      } else {
        phrases += ""
      }
    }
    phrases
  }

  //Filter a list of phrases for finer grained meaning of phraseTags
  private def filterSentencePhrase(patternType:String,phraseTags:List[String],phrases:Seq[String],positions:Seq[(Int,Int)]):Seq[SentencePhrase] = {
    val sentencePhrases = new ArrayBuffer[SentencePhrase]()
    phraseTags.foreach { phraseTag =>
      phrases.filter(_.length < 35).zipWithIndex.foreach { case(phrase,idx) =>
        if(phrase.nonEmpty && PhraseTag.filter(phraseTag,phrase)) sentencePhrases += new SentencePhrase(phraseTag, phrase, positions.apply(idx)._1, positions.apply(idx)._2)
      }
    }
    sentencePhrases
  }


  // Get the start and end positions of the postag pattern
  private def findStartRepeatPatterns(tags: Seq[String], tagList: List[String]): Seq[(Int, Int)] = {
    val startTag = tagList.apply(0)
    val repeatTags = tagList.drop(1)
    val patterns = ArrayBuffer[(Int, Int)]()
    var started = false; var s = -1; var e = -1
    tags.zipWithIndex.foreach { case (tag, i) =>
      if (!started) {
        if (compare(tag, startTag, false)) {
          s = i
          started = true
        }
      } else {
        e = i
        if (!matchAny(tag, repeatTags, false)) {
          if(s!=e) patterns += ((s, e - 1))
          started = false;
          s = -1;
          e = -1
        }
      }
    }
    patterns
  }

  // Get the start and end positions of the postag pattern
  private def findStartEndPatterns(tags: Seq[String], tagList: List[String]): Seq[(Int, Int)] = {
    val startTag = tagList.apply(0)
    val endTag = tagList.apply(1)
    val patterns: ArrayBuffer[(Int, Int)] = ArrayBuffer()
    var started = false;
    var s = -1;
    var e = -1
    tags.zipWithIndex.foreach { case (tag, i) =>
      if (!started) {
        if (compare(tag, startTag, true)) {
          s = i
          started = true
        }
      } else {
        e = i
        if (compare(tag, endTag, true)) {
          if(s!=e) patterns += ((s, e))
          started = false;
          s = -1;
          e = -1
        }
      }
    }
    patterns
  }

  private def matchAny(tag: String, repeatTags: List[String], exact: Boolean) = {
    var found = false
    repeatTags.foreach { rep =>
      if (compare(tag, rep, exact)) found = true
    }
    found
  }

  private def compare(tag1: String, tag2: String, exact: Boolean) = {
    if (exact) tag1.equalsIgnoreCase(tag2)
    else tag1.contains(tag2)
  }

}


