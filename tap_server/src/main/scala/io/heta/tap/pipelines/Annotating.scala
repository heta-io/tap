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
import akka.actor.ActorRef
import akka.pattern.ask
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import com.typesafe.scalalogging.Logger
import io.heta.tap.analysis.Syllable
import io.heta.tap.analysis.clu.CluAnnotatorActor.AnnotateRequest
import io.heta.tap.analysis.expression.ExpressionAnalyser
import io.heta.tap.analysis.languagetool.Speller
import io.heta.tap.data.doc.expression.Expressions
import io.heta.tap.data.doc.expression.affect.{AffectExpression, AffectExpressions, AffectThresholds}
import io.heta.tap.data.doc.expression.reflect._
import io.heta.tap.data.doc.spell.Spelling
import io.heta.tap.data.doc.vocabulary.{TermCount, Vocabulary}
import io.heta.tap.data.doc.{expression, _}
import io.heta.tap.{analysis, pipelines}
import io.heta.tap.pipelines.AnnotatingTypes._
import io.heta.tap.pipelines.materialize.PipelineContext
import io.nlytx.expressions.ReflectiveExpressionPipeline
import io.nlytx.nlp.api.AnnotatorPipelines
import io.nlytx.nlp.api.DocumentModel.{Document, Token}
import org.clulab.processors

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

/**
  * Created by andrew@andrewresearch.net on 6/9/17.
  */

class Annotating(cluAnnotator:ActorRef) {

  //val languageTool: ActorRef = ???
  //val expressions:analysis.expression.ExpressionAnalyser = new analysis.expression.ExpressionAnalyser()
  //val ca:CluAnnotatorActor = new CluAnnotatorActor()

  val logger: Logger = Logger(this.getClass)

  val parallelism = 5

  //private val cluAnnotator = PipelineContext.system.actorSelection("user/cluAnnotator")

  /* The main pipelines for performing annotating text analysis */
  object Pipeline {
    val cluSentences = cluTapSentences
    val sentences  = tapSentences
    val vocab = tapSentences via tapVocab
    val metrics = tapSentences via tapMetrics
    val expressions = tapSentences via tapExpressions
    val syllables = tapSentences via tapSyllables
    val spelling = tapSentences via tapSpelling
    val posStats = tapSentences via tapPosStats
    val reflectExpress = reflectExpressions
    def affectExpress(thresholds:Option[AffectThresholds] = None) = cluTapSentences via affectExpressions(thresholds)
  }

  def build[A,B](pipetype:String,pipeline: Flow[A,B,NotUsed]):Flow[String,B,NotUsed] = {
    val makeDoc:Flow[String,A,NotUsed] = pipetype match {
      case STANDARD => makeDocument.asInstanceOf[Flow[String,A,NotUsed]]
      case FAST => makeFastDocument.asInstanceOf[Flow[String,A,NotUsed]]
      case NER => makeNerDocument.asInstanceOf[Flow[String,A,NotUsed]]
      case CLU => makeCluDocument.asInstanceOf[Flow[String,A,NotUsed]]
      case _ => makeFastDocument.asInstanceOf[Flow[String,A,NotUsed]]
    }
    makeDoc via pipeline
  }

  /* Initialise required analytics processes */

  /* Running LanguageTool in an Actor */ //TODO Perhaps look at a streamed implementation?
//  implicit val timeout: Timeout = 120 seconds
//  val languageToolInitialised:Future[Boolean] = ask(languageTool,INIT).mapTo[Boolean]
//  languageToolInitialised.onComplete {
//    case Success(result) => logger.info(s"LanguageTool initialised successfully: $result")
//    case Failure(e) => logger.error("LanguageTool encountered an error on startup: " + e.toString)
//  }

  import PipelineContext.executor

  private val ap = AnnotatorPipelines

  {

    /* Initialise Factorie models by running a test through docBuilder */
    logger.info("Initialising Factorie models")
    val docStart = Future(ap.profile("Please start Factorie!", ap.fastPipeline))
    docStart.onComplete {
      case Success(doc) => logger.info(s"Factorie started successfully [${doc.tokenCount} tokens]")
      case Failure(e) => logger.error("Factorie start failure:" + e.toString)
    }

  }



  /* The following are pipeline segments that can be re-used in the above pipelines
   *
   */

  val makeCluDocument = Flow[String]
    .mapAsync[org.clulab.processors.Document](parallelism) { text =>
    implicit val timeout: Timeout = 5.seconds
    (cluAnnotator ? AnnotateRequest(text)).mapTo[org.clulab.processors.Document]
  }

  val makeDocument = Flow[String]
    .mapAsync[Document](2) { text =>
    ap.process(text,ap.parserPipeline)
  }

  //If parser info is NOT required
  val makeFastDocument = Flow[String]
    .mapAsync[Document](2) { text =>
    ap.process(text,ap.defaultPipeline)
  }

  //If nertags ARE required
  val makeNerDocument = Flow[String]
    .mapAsync[Document](2) { text =>
    ap.process(text,ap.completePipeline)
  }

  val sections: Flow[Document,Sections,NotUsed] = Flow[Document]
    .map(_.sections.toVector)

  val cluTapSentences: Flow[processors.Document, Vector[Sentence], NotUsed] = Flow[org.clulab.processors.Document]
      .map { doc =>
        logger.info("Extracting sentences")
        doc.sentences
      }
    .map { sentArray =>
      sentArray.toList.zipWithIndex.map { case (s, idx) =>
        val tokens = getTokens(s.startOffsets,s.words,s.lemmas,s.tags,s.entities)
        Sentence(s.getSentenceText,tokens,-1,-1,s.words.length,idx)
      }.toVector
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

  val tapSentences: Flow[Document, Vector[Sentence], NotUsed] = Flow[Document]
    .map { doc =>
      doc.sentences
    }
    .map { sentList =>
      sentList.zipWithIndex.map { case(s,idx) =>
        val tokens = s.tokens.toVector.map { t =>
          val (children,parent,parseLabel) = getParseData(t).toOption.getOrElse((Vector(),-1,""))
          val nerTag = Try(t.nerTag.baseCategoryValue).toOption.getOrElse("")
          Token(t.positionInSentence,t.string,t.lemmaString,t.posTag.value.toString,
            nerTag,parent,children,parseLabel,t.isPunctuation)
        }
        Sentence(s.documentString ,tokens, s.start, s.end, s.length, idx)
      }.toVector
    }

  private def getParseData(t:Token):Try[(Vector[Int],Int,String)] = Try {
    (t.parseChildren.toVector.map(_.positionInSentence),t.parseParentIndex,t.parseLabel.value.toString)
  }

  val tapVocab: Flow[TapSentences, Vocabulary, NotUsed] = Flow[TapSentences]
    .map { v =>
      v.flatMap(_.tokens)
        .map(_.term.toLowerCase)
        .groupBy((term: String) => term)
        .mapValues(_.length)
    }.map { m =>
    val lst: Vector[TermCount] = m.toVector.map { case (k, v) => TermCount(k, v) }
    Vocabulary(m.size, lst)
  }

  val tapMetrics: Flow[TapSentences, Metrics, NotUsed] = Flow[TapSentences]
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

      Metrics(res.length, res.map(_._1).sum, wordCount,res.map(_._3).sum, res.map(_._4).sum, res.map(_._5).sum,
        sentWordCounts, averageSentWordCount, wordLengths ,averageWordLength,averageSentWordLength)
    }

  val tapExpressions: Flow[TapSentences, Vector[expression.Expressions], NotUsed] = Flow[TapSentences]
    .mapAsync[Vector[expression.Expressions]](3) { v =>
    val results = v.map { sent =>
      for {
        ae <- ExpressionAnalyser.affect(sent.tokens)
        ee <- ExpressionAnalyser.epistemic(sent.tokens)
        me <- ExpressionAnalyser.modal(sent.tokens)
      } yield Expressions(ae, ee, me, sent.idx)
    }
    Future.sequence(results)
  }

  val tapSyllables: Flow[TapSentences,Vector[Syllables], NotUsed] = Flow[TapSentences]
    .map { v =>
      v.map { sent =>
        val counts = sent.tokens.map( t => Syllable.count(t.term.toLowerCase)).filterNot(_ == 0)
        val avg = counts.sum / sent.tokens.length.toDouble
        Syllables(sent.idx,avg,counts)
      }
    }


  val tapSpelling: Flow[TapSentences, Vector[Spelling], NotUsed] = Flow[TapSentences]
    .mapAsync[Vector[Spelling]](2) { v =>
    val sents = v.map(sent => Sentence(sent.original,sent.tokens,sent.start,sent.end,sent.length,sent.idx))
    Speller.check(sents)
  }


  val tapPosStats:Flow[TapSentences, PosStats, NotUsed] = Flow[TapSentences]
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
      PosStats(verbNounRatio,futurePastRatio,nerWordRatio,adjWordRatio,nounDist,verbDist,adjDist)
    }

  val reflectExpressions:Flow[Document,ReflectExpressions,NotUsed] = Flow[Document]
    .map { doc =>
      val REP = ReflectiveExpressionPipeline
      val codedSents = REP.getCodedSents(doc)
      val tags = REP.getCoded(codedSents)
      val reflect = REP.getReflect(doc)
      val summary =  REP.getSummary(codedSents)
      val metaMap = summary.metaTagSummary
      val phraseMap = summary.phraseTagSummary
      val metaTagSummary = MetaTagSummary(
        metaMap.getOrElse("knowledge",0),
        metaMap.getOrElse("experience",0),
        metaMap.getOrElse("regulation",0),
        metaMap.getOrElse("none",0)
      )
      val phraseTagSummary = PhraseTagSummary(
        phraseMap.getOrElse("outcome", 0),
        phraseMap.getOrElse("temporal", 0),
        phraseMap.getOrElse("pertains", 0),
        phraseMap.getOrElse("consider", 0),
        phraseMap.getOrElse("anticipate", 0),
        phraseMap.getOrElse("definite", 0),
        phraseMap.getOrElse("possible", 0),
        phraseMap.getOrElse("selfReflexive", 0),
        phraseMap.getOrElse("emotive", 0),
        phraseMap.getOrElse("selfPossessive", 0),
        phraseMap.getOrElse("compare", 0),
        phraseMap.getOrElse("manner", 0),
        phraseMap.getOrElse("none", 0)
      )
     ReflectExpressions(
       WordSentenceCounts(reflect.wordCount,reflect.avgWordLength,reflect.sentenceCount,reflect.avgSentenceLength),
       Summary(metaTagSummary,phraseTagSummary),
       tags.map(c => SentencePhrasesTags(c.sentence,c.phrases,c.subTags,c.metaTags)))
    }

  def affectExpressions(thresholds:Option[AffectThresholds] = None):Flow[TapSentences,Vector[AffectExpressions],NotUsed] = {
    val th = thresholds.getOrElse(AffectThresholds(arousal=4.95,valence = 0.0,dominance = 0.0))
    Flow[TapSentences].mapAsync[Vector[AffectExpressions]](3) { sents =>
      val results = sents.map { s =>
        for {
          ae <- ExpressionAnalyser.affective(s.tokens)
        } yield AffectExpressions(filterAffectThresholds(ae,th),s.idx)
      }
      Future.sequence(results)
    }
  }

  private def filterAffectThresholds(affectExpressions:Vector[AffectExpression], thresholds:AffectThresholds) = {
    affectExpressions.filter{ ae =>
      ae.valence >= thresholds.valence &&
      ae.arousal >= thresholds.arousal &&
        ae.dominance >= thresholds.dominance
    }
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