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
import cc.factorie.app.nlp.{Document, Section}
import play.api.Logger
import tap.analysis.Syllable
import tap.data._
import tap.nlp.factorie.FactorieAnnotatorActor.{INIT, MakeDocument}
import tap.nlp.factorie.LanguageToolActor.CheckSpelling

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by andrew@andrewresearch.net on 6/9/17.
  */
class Annotating @Inject()(
                            @Named("factorie-annotator") factorieAnnotator: ActorRef,
                            @Named("languagetool") languageTool: ActorRef
                          ) {

  implicit val timeout: Timeout = 300.seconds
  factorieAnnotator ! INIT //No return value
  val languageToolInitialised:Future[Boolean] = ask(languageTool,INIT).mapTo[Boolean]

//  annotatorInitialised.onComplete{
//    case Success(result) => Logger.info(s"FactorieAnnotator initialised successfully: $result")
//    case Failure(e) => Logger.error(s"FactorieAnnotator failed to initialise within ${timeout.toString} due to error: ${e.printStackTrace}")
//  }
  languageToolInitialised.onComplete{
    case Success(result) => Logger.info(s"LanguageTool initialised successfully: $result")
    case Failure(e) => Logger.error(s"LanguageTool failed to initialise within ${timeout.toString} due to error: ${e.printStackTrace}")
  }

  object Pipeline {
    val sentences: Flow[String, List[TapSentence], NotUsed] = makeDocument via tapSentences
    val vocab: Flow[String, TapVocab, NotUsed] = makeDocument via tapSentences via tapVocab
    val metrics: Flow[String, TapMetrics, NotUsed] = makeDocument via tapSentences via tapMetrics
    val expressions: Flow[String, List[TapExpressions], NotUsed] = makeDocument via tapSentences via tapExpressions
    val syllables: Flow[String,List[TapSyllables],NotUsed] = makeDocument via tapSentences via tapSyllables
    val spelling: Flow[String,List[TapSpelling],NotUsed] = makeDocument via tapSentences via tapSpelling
    val posStats: Flow[String, TapPosStats, NotUsed] = makeDocument via tapSentences via tapPosStats
  }

  val makeDocument: Flow[String, Document, NotUsed] = Flow[String]
    .mapAsync[Document](2) { text =>
      ask(factorieAnnotator,MakeDocument(text)).mapTo[Future[Document]].flatMap(identity)
    }

  val sections: Flow[Document,List[Section],NotUsed] = Flow[Document]
    .map(_.sections.toList)

  val tapSentences: Flow[Document, List[TapSentence], NotUsed] = Flow[Document]
      .map { doc =>
        doc.sentences.toList
      }
      .map { sentList =>
        sentList.zipWithIndex.map { case(s,idx) =>
          val tokens = s.tokens.toList.map { t =>
            TapToken(t.positionInSentence,t.string,t.lemmaString,t.posTag.value.toString,
              t.nerTag.baseCategoryValue,t.parseParentIndex,0,t.parseLabel.value.toString(),t.isPunctuation)
          }.toVector
          TapSentence(s.documentString ,tokens, s.start, s.end, s.length, idx)
        }
      }



  val tapVocab: Flow[List[TapSentence], TapVocab, NotUsed] = Flow[List[TapSentence]]
      .map { lst =>
        lst.flatMap(_.tokens)
          .map(_.term.toLowerCase)
          .groupBy((term: String) => term)
          .mapValues(_.length)
      }.map { m =>
      val lst: List[TermCount] = m.toList.map { case (k, v) => TermCount(k, v) }
      TapVocab(m.size, lst)
    }

  val tapMetrics: Flow[List[TapSentence], TapMetrics, NotUsed] = Flow[List[TapSentence]]
      .map { lst =>
        lst.map { s =>
          val tokens:Int = s.tokens.length
          val characters:Int = s.original.length
          val punctuation:Int = s.tokens.count(_.isPunctuation)
          val words:Int = (tokens - punctuation)
          val wordLengths:Vector[Int] = s.tokens.filterNot(_.isPunctuation).map(_.term.length)
          val totalWordChars = wordLengths.sum
          val whitespace:Int = s.original.count(_.toString.matches("\\s"))
          val averageWordLength:Double = wordLengths.sum / words.toDouble
          (tokens,words,characters,punctuation,whitespace,wordLengths,averageWordLength)
        }
      }
      .map { res =>
        val sentCount:Int = res.length
        val sentWordCounts = res.map(_._2).toVector
        val wordCount = sentWordCounts.sum
        val averageSentWordCount = wordCount / sentCount.toDouble
        val wordLengths = res.map(_._6).toVector
        val averageWordLength = wordLengths.flatten.sum / wordCount.toDouble
        val averageSentWordLength = res.map(_._7).toVector

        TapMetrics(res.length, res.map(_._1).sum, wordCount,res.map(_._3).sum, res.map(_._4).sum, res.map(_._5).sum,
          sentWordCounts, averageSentWordCount, wordLengths ,averageWordLength,averageSentWordLength)
      }

  val tapExpressions: Flow[List[TapSentence], List[TapExpressions], NotUsed] = Flow[List[TapSentence]]
    .mapAsync[List[TapExpressions]](3) { lst =>
      val results = lst.map { sent =>
        for {
            ae <- Expressions.affect(sent.tokens)
            ee <- Expressions.epistemic(sent.tokens)
            me <- Expressions.modal(sent.tokens)
        } yield (TapExpressions(ae, ee, me, sent.idx))
      }
      Future.sequence(results)
    }

  val tapSyllables: Flow[List[TapSentence],List[TapSyllables], NotUsed] = Flow[List[TapSentence]]
    .map { lst =>
      lst.map { sent =>
        val counts = sent.tokens.map( t => Syllable.count(t.term.toLowerCase)).filterNot(_ == 0)
        val avg = counts.sum / (sent.tokens.length).toDouble
        TapSyllables(sent.idx,avg,counts)
      }
    }

  val tapSpelling: Flow[List[TapSentence],List[TapSpelling],NotUsed] = Flow[List[TapSentence]]
    .mapAsync[List[TapSpelling]](2) { lst =>
      val checked = lst.map { sent =>
        ask(languageTool,CheckSpelling(sent.original)).mapTo[Vector[TapSpell]].map(sp => TapSpelling(sent.idx,sp))
      }
      Future.sequence(checked)
    }


  val tapPosStats:Flow[List[TapSentence], TapPosStats, NotUsed] = Flow[List[TapSentence]]
    .map { lst =>
      val stats = lst.map { s =>
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
      val verbDist = verbs.map(_ / verbs.sum.toDouble).toVector
      val nouns = stats.map(_._4)
      val nounDist = nouns.map(_ / nouns.sum.toDouble).toVector
      val adjs = stats.map(_._5)
      val adjDist = adjs.map(_ / adjs.sum.toDouble).toVector
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