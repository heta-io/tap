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

import javax.inject.{Inject, Named}

import akka.NotUsed
import akka.actor.ActorRef
import akka.pattern.ask
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import io.nlytx.nlp.api.AnnotatorPipelines
import io.nlytx.nlp.api.DocumentModel.{Document, Token}
import play.api.Logger
import tap.analysis.Syllable
import tap.data._ // scalastyle:ignore
import tap.nlp.factorie.LanguageToolActor.{CheckSpelling, INIT}
import tap.pipelines.AnnotatingTypes._ // scalastyle:ignore

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

/**
  * Created by andrew@andrewresearch.net on 6/9/17.
  */

class Annotating @Inject()(@Named("languagetool") languageTool: ActorRef, expressions:Expressions) {

  val logger: Logger = Logger(this.getClass)

  /* The main pipelines for performing annotating text analysis */
  object Pipeline {
    val sentences: SentencesFlow  = tapSentences
    val vocab: VocabFlow = tapSentences via tapVocab
    val metrics: MetricsFlow = tapSentences via tapMetrics
    val expressions: ExpressionsFlow = tapSentences via tapExpressions
    val syllables: SyllablesFlow = tapSentences via tapSyllables
    val spelling: SpellingFlow = tapSentences via tapSpelling
    val posStats: PosStatsFlow = tapSentences via tapPosStats
  }

  def build[T](pipetype:String,pipeline: Flow[Document,T,NotUsed]):Flow[String,T,NotUsed] = {
    val makeDoc = pipetype match {
      case STANDARD => makeDocument
      case FAST => makeFastDocument
      case NER => makeNerDocument
      case _ => makeFastDocument
    }
    makeDoc via pipeline
  }

  /* Initialise required analytics processes */

  /* Running LanguageTool in an Actor */ //TODO Perhaps look at a streamed implementation?
  implicit val timeout: Timeout = 120 seconds
  val languageToolInitialised:Future[Boolean] = ask(languageTool,INIT).mapTo[Boolean]
  languageToolInitialised.onComplete {
    case Success(result) => logger.info(s"LanguageTool initialised successfully: $result")
    case Failure(e) => logger.error("LanguageTool encountered an error on startup: " + e.toString)
  }

  /* Initialise Factorie models by running a test through docBuilder */
  logger.info("Initialising Factorie models")
  private val ap = AnnotatorPipelines
  val docStart = Future(ap.profile("Please start Factorie!",ap.fastPipeline))
  docStart.onComplete{
    case Success(doc) => logger.info(s"Factorie started successfully [${doc.tokenCount} tokens]")
    case Failure(e) => logger.error("Factorie start failure:" + e.toString)
  }






  /* The following are pipeline segments that can be re-used in the above pipelines
   *
   */

  val makeDocument: DocumentFlow = Flow[String]
    .mapAsync[Document](2) { text =>
    ap.process(text,ap.parserPipeline)
  }

  //If parser info is NOT required
  val makeFastDocument: DocumentFlow = Flow[String]
    .mapAsync[Document](2) { text =>
    ap.process(text,ap.defaultPipeline)
  }

  //If nertags ARE required
  val makeNerDocument: DocumentFlow = Flow[String]
    .mapAsync[Document](2) { text =>
    ap.process(text,ap.completePipeline)
  }

  val sections: Flow[Document,Sections,NotUsed] = Flow[Document]
    .map(_.sections.toVector)

  val tapSentences: SentencesFlow = Flow[Document]
    .map { doc =>
      doc.sentences
    }
    .map { sentList =>
      sentList.zipWithIndex.map { case(s,idx) =>
        val tokens = s.tokens.toVector.map { t =>
          val (children,parent,parseLabel) = getParseData(t).toOption.getOrElse((Vector(),-1,""))
          val nerTag = Try(t.nerTag.baseCategoryValue).toOption.getOrElse("")
          TapToken(t.positionInSentence,t.string,t.lemmaString,t.posTag.value.toString,
            nerTag,parent,children,parseLabel,t.isPunctuation)
        }
        TapSentence(s.documentString ,tokens, s.start, s.end, s.length, idx)
      }.toVector
    }

  private def getParseData(t:Token):Try[(Vector[Int],Int,String)] = Try {
    (t.parseChildren.toVector.map(_.positionInSentence),t.parseParentIndex,t.parseLabel.value.toString)
  }

  val tapVocab: Flow[TapSentences, TapVocab, NotUsed] = Flow[TapSentences]
    .map { v =>
      v.flatMap(_.tokens)
        .map(_.term.toLowerCase)
        .groupBy((term: String) => term)
        .mapValues(_.length)
    }.map { m =>
    val lst: Vector[TermCount] = m.toVector.map { case (k, v) => TermCount(k, v) }
    TapVocab(m.size, lst)
  }

  val tapMetrics: Flow[TapSentences, TapMetrics, NotUsed] = Flow[TapSentences]
    .map { v =>
      v.map { s =>
        val tokens:Int = s.tokens.length
        val characters:Int = s.original.length
        val punctuation:Int = s.tokens.count(_.isPunctuation)
        val words:Int = tokens - punctuation
        val wordLengths:Vector[Int] = s.tokens.filterNot(_.isPunctuation).map(_.term.length)
        //val totalWordChars = wordLengths.sum
        val whitespace:Int = s.original.count(_.toString.matches("\\s"))
        val averageWordLength:Double = wordLengths.sum / words.toDouble
        (tokens,words,characters,punctuation,whitespace,wordLengths,averageWordLength)
      }
    }
    .map { res =>
      val sentCount:Int = res.length
      val sentWordCounts = res.map(_._2)
      val wordCount = sentWordCounts.sum
      val averageSentWordCount = wordCount / sentCount.toDouble
      val wordLengths = res.map(_._6)
      val averageWordLength = wordLengths.flatten.sum / wordCount.toDouble
      val averageSentWordLength = res.map(_._7)

      TapMetrics(res.length, res.map(_._1).sum, wordCount,res.map(_._3).sum, res.map(_._4).sum, res.map(_._5).sum,
        sentWordCounts, averageSentWordCount, wordLengths ,averageWordLength,averageSentWordLength)
    }

  val tapExpressions: Flow[TapSentences, Vector[TapExpressions], NotUsed] = Flow[TapSentences]
    .mapAsync[Vector[TapExpressions]](3) { v =>
    val results = v.map { sent =>
      for {
        ae <- expressions.affect(sent.tokens)
        ee <- expressions.epistemic(sent.tokens)
        me <- expressions.modal(sent.tokens)
      } yield TapExpressions(ae, ee, me, sent.idx)
    }
    Future.sequence(results)
  }

  val tapSyllables: Flow[TapSentences,Vector[TapSyllables], NotUsed] = Flow[TapSentences]
    .map { v =>
      v.map { sent =>
        val counts = sent.tokens.map( t => Syllable.count(t.term.toLowerCase)).filterNot(_ == 0)
        val avg = counts.sum / sent.tokens.length.toDouble
        TapSyllables(sent.idx,avg,counts)
      }
    }

  val tapSpelling: Flow[TapSentences,Vector[TapSpelling],NotUsed] = Flow[TapSentences]
    .mapAsync[Vector[TapSpelling]](2) { v =>
    val checked = v.map { sent =>
      ask(languageTool,CheckSpelling(sent.original)).mapTo[Vector[TapSpell]].map(sp => TapSpelling(sent.idx,sp))
    }
    Future.sequence(checked)
  }


  val tapPosStats:Flow[TapSentences, TapPosStats, NotUsed] = Flow[TapSentences]
    .map { v =>
      val stats = v.map { s =>
        val ts = s.tokens
        val tokens:Int = ts.length
        val punctuation:Int = ts.count(_.isPunctuation)
        val words: Int = tokens - punctuation
        val verbs = ts.count(_.postag.contains("VB"))
        val nouns = ts.count(_.postag.contains("NN"))
        val adjectives = ts.count(_.postag.contains("JJ"))
        val ner = ts.filterNot(_.nertag.contentEquals("O")).length
        (words,ner,verbs,nouns,adjectives)
      }
      val words = stats.map(_._1)
      val ners = stats.map(_._2)
      val verbs = stats.map(_._3)
      val verbDist = verbs.map(_ / verbs.sum.toDouble)
      val nouns = stats.map(_._4)
      val nounDist = nouns.map(_ / nouns.sum.toDouble)
      val adjs = stats.map(_._5)
      val adjDist = adjs.map(_ / adjs.sum.toDouble)
      val verbNounRatio = verbs.sum / nouns.sum.toDouble
      val futurePastRatio = 0.0
      val nerWordRatio = ners.sum / words.sum.toDouble
      val adjWordRatio = adjs.sum / words.sum.toDouble
      TapPosStats(verbNounRatio,futurePastRatio,nerWordRatio,adjWordRatio,nounDist,verbDist,adjDist)
    }


}



/*
object Vocab {

  val document:Flow[OldTapDocument,List[OldTapSection],NotUsed] = Flow[OldTapDocument].map(_.sections)

  val sectionsVocab:Flow[List[OldTapSection],List[Map[String,Int]],NotUsed] = Flow[List[OldTapSection]].map(_.map(_.sentences.flatMap(_.tokens).groupBy((word:String) => word).mapValues(_.length)))
  val documentVocab = Flow[List[Map[String,Int]]].fold(Map[String,Int]())(_ ++ _.flatten)

  val vocabByCount = Flow[Map[String,Int]].map(_.toList.groupBy(_._2).map(wc => wc._1 -> wc._2.map(_._1)).toSeq.reverse).map(l=> ListMap(l:_*))

  val pipeline = document.via(sectionsVocab).via(documentVocab).via(vocabByCount)

}

private def complexity(metrics:Metrics,vocab:Vocab,syllables:Syllables):Complexity = {
    val selectVocab = vocab.countVocab.map(_._2).flatten.filterNot(_.length < 4).toList
    val vocabToDocRatio = selectVocab.length / metrics.wordCount.toDouble
    val avgSentLength = metrics.wordCount / metrics.sentenceCount.toDouble
    val avgWordLength = metrics.characterCount / metrics.wordCount.toDouble
    val avgSyllables = syllables.averageSyllables
    GenericComplexity(vocabToDocRatio,avgSentLength,avgWordLength,avgSyllables)
  }
*/