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

package io.heta.tap.pipelines

import akka.NotUsed
import akka.stream.scaladsl.Flow
import akka.util.ByteString
import com.typesafe.scalalogging.Logger
import io.heta.tap.analysis.affectlexicon.AffectLexicon
import io.heta.tap.analysis.reflectiveExpressions.PosTagAnalyser
import io.heta.tap.data.doc._
import io.heta.tap.data.doc.expression.affect.{AffectExpression, AffectExpressions, AffectThresholds}
import io.heta.tap.data.doc.expression.reflect._
import io.heta.tap.data.results.{AffectExpressionsBatchResult, ReflectExpressionsBatchResult, SentencesBatchResult}
import io.heta.tap.pipelines.materialize.FilePipeline.File
import org.clulab.processors.Document
import play.api.libs.json.Json

/*
These are Flow Segments that are joined together to make Pipes
 */
object Segment {

  private val logger: Logger = Logger(this.getClass)

  val Document_SentencesBatchResult: Flow[Document, SentencesBatchResult, NotUsed] = Flow[org.clulab.processors.Document]
    .map { doc =>
      logger.info("Extracting sentences")
      val sents = doc.sentences.toList.zipWithIndex.map { case (s, idx) =>
        val tokens = getTokens(s.startOffsets,s.words,s.lemmas,s.tags,s.entities)
        Sentence(s.getSentenceText,tokens,-1,-1,s.words.length,idx)
      }.toVector
      SentencesBatchResult(doc.id.getOrElse("unknown"),sents)
    }

  val AnalyticsResult_File: Flow[AnalyticsResult,File,NotUsed] = Flow[AnalyticsResult]
    .map[File](ar => File(ar.name,ByteString(Json.prettyPrint(ar.asJson))))


  def SentencesBatchResult_AffectExpressionsBatchResult(thresholds:Option[AffectThresholds] = None): Flow[SentencesBatchResult, AffectExpressionsBatchResult, NotUsed] = {
    val th = thresholds.getOrElse(AffectThresholds(arousal=4.95,valence = 0.0,dominance = 0.0))
    Flow[SentencesBatchResult].map[AffectExpressionsBatchResult] { sents =>
      val aes = sents.analytics.map { s =>
        val ae = AffectLexicon.getAllMatchingTerms(s.tokens)
        AffectExpressions(filterAffectThresholds(ae,th),s.idx)
      }
      AffectExpressionsBatchResult(sents.name,aes)
    }
  }



  val Document_ReflectiveExpressionsBatchResult: Flow[Document, ReflectExpressionsBatchResult, NotUsed] = Flow[Document].map { doc =>
    val codedSents = getCodedSents(doc)
    val reflectExpressions = ReflectExpressions(getReflect(doc), getSummary(codedSents), getCoded(codedSents))
    ReflectExpressionsBatchResult(doc.id.getOrElse(""),reflectExpressions)
  }

  private def getReflect(doc: Document): WordSentenceCounts = {
    val sents = doc.sentences.toVector
    val words = sents.map(s => s.words)
    val wordLengths = words.map(_.length)
    val wc = words.length
    val awl = wordLengths.sum / wc.toDouble
    val sc = sents.length
    val asl = wc / sc.toDouble
    WordSentenceCounts(wc, awl, sc, asl)
  }

  private def getSummary(codedSents: Seq[CodedSentence]): Summary = {
    var mts: Map[String, Int] = codedSents.flatMap(_.metacognitionTags).groupBy(identity).mapValues(_.size)
    mts += "none" -> codedSents.count(_.metacognitionTags.length < 1)
    Array("knowledge", "experience", "regulation").foreach { k =>
      if (!mts.contains(k)) mts += k -> 0
    }
    val metaTagSummary = MetaTagSummary(mts.getOrElse("knowledge",0),mts.getOrElse("experience",0),mts.getOrElse("regulation",0),mts.getOrElse("none",0))
    var pts = codedSents.flatMap(_.phraseTags).filterNot(_.contains("general")).groupBy(identity).mapValues(_.size)
    pts += "none" -> codedSents.count(_.phraseTags.length < 1)
    Array("outcome", "temporal", "pertains", "consider", "anticipate", "definite",
      "possible", "selfReflexive", "emotive", "selfPossessive", "compare", "manner").foreach { k =>
      if (!pts.contains(k)) pts += k -> 0
    }
    val phraseTagSummary = PhraseTagSummary(
      pts.getOrElse("outcome",0), pts.getOrElse("temporal",0), pts.getOrElse("pertains",0),
      pts.getOrElse("consider",0), pts.getOrElse("anticipate",0), pts.getOrElse("definite",0),
      pts.getOrElse("possible",0), pts.getOrElse("selfReflexive",0), pts.getOrElse("emotive",0),
      pts.getOrElse("selfPossessive",0), pts.getOrElse("compare",0), pts.getOrElse("manner",0),
      pts.getOrElse("none",0)
    )
    Summary(metaTagSummary, phraseTagSummary)
  }

  private def getCodedSents(doc: Document): Seq[CodedSentence] = {
    val docSentences = doc.sentences.toSeq
    val sentencePosTags = docSentences.map(_.tags.getOrElse(Array()).toSeq)
    val sentenceWords = docSentences.map(_.words.toSeq)
    PosTagAnalyser.analyse(sentencePosTags,sentenceWords)
  }

  private def getCoded(codedSentences: Seq[CodedSentence]): Seq[SentencePhrasesTags] = {
    codedSentences.map { cs =>
      SentencePhrasesTags(
        cs.sentence,
        cs.phrases.map(p => p.phrase + "[" + p.phraseType + "," + p.start + "," + p.end + "]"),
        //cs.phraseTags,
        cs.subTags,
        cs.metacognitionTags
      )
    }
  }

  private def filterAffectThresholds(affectExpressions:Vector[AffectExpression], thresholds:AffectThresholds) = {
    affectExpressions.filter{ ae =>
      ae.valence >= thresholds.valence &&
        ae.arousal >= thresholds.arousal &&
        ae.dominance >= thresholds.dominance
    }
  }

  private def getTokens(start:Array[Int],words:Array[String],
                        lemmas:Option[Array[String]],posTags:Option[Array[String]],nerTags:Option[Array[String]]) = {
    val numTokens = words.length
    val is = List.range(0, numTokens)
    val ws = words.toVector
    logger.debug(words.mkString("|"))
    val ls = lemmas.map(_.toVector).getOrElse(Vector.fill(numTokens)(""))
    val pts = posTags.map(_.toVector).getOrElse(Vector.fill(numTokens)(""))
    logger.debug(pts.mkString("|"))
    val nts = nerTags.map(_.toVector).getOrElse(Vector.fill(numTokens)(""))

    val tapTokens = for {
      ((((i,w),l),pt),nt) <- is zip ws zip ls zip pts zip nts
    } yield Token(i,w,l,pt,nt,-1,Vector(),"",false)

    tapTokens.toVector
  }



}
