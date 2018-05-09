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

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import io.nlytx.nlp.api.AnnotatorPipelines
import io.nlytx.nlp.api.DocumentModel.Document
import org.scalatestplus.play.PlaySpec
import play.api.inject.guice.GuiceApplicationBuilder
import tap.data._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


class AnnotatingPipelineSpec extends PlaySpec {

  val ap = AnnotatorPipelines

  //dependency injection
  private val app = new GuiceApplicationBuilder().build
  private val annotator = app.injector.instanceOf[Annotating]

  "profile" should {
     "create an annotated document" in {
       val doc = ap.profile("This is a test!")
       assert(doc.tokenCount==5)
     }
  }

  import tap.pipelines.materialize.PipelineContext.materializer

  val docFlow = annotator.makeDocument
  "annotator" should {


    "make a valid document flow" in {

      assert(docFlow.isInstanceOf[Flow[String,Document,NotUsed]])
    }

    "materialize the flow" in {

      //val docFlow = annotator.makeDocument
      val graph = Source.single("This is a test.").via(docFlow).toMat(Sink.head[Document])(Keep.right)
      val result:Future[Document] = graph.run()
      val doc = Await.result(result, 240 seconds)
      assert(doc.tokenCount==5)
    }
  }

  val sentenceFlow = annotator.tapSentences
  "sentences" should {
    "extract separated sentences" in {

      val input = s"How can I convert a Scala array to a String? Or, more, accurately, how do I convert any Scala sequence to a String."

      val graph = Source.single(input).via(docFlow).via(sentenceFlow).toMat(Sink.head[Vector[TapSentence]])(Keep.right)
      val result:Future[Vector[TapSentence]] = graph.run()
      val sent = Await.result(result, 240 seconds)

      assert(sent.length == 2)
      assert(sent(0).original == "How can I convert a Scala array to a String?")
      assert(sent(1).original == "Or, more, accurately, how do I convert any Scala sequence to a String.")
    }
  }


  "vocab" should {
    "extract vocabularies and their frequency" in {

      val vocabFlow = annotator.tapVocab

      val input = s"Or, more, accurately, how do I convert any Scala sequence to a String."
      val graph = Source.single(input).via(docFlow).via(sentenceFlow).via(vocabFlow).toMat(Sink.head[TapVocab])(Keep.right)
      val result:Future[TapVocab] = graph.run()
      val vocab = Await.result(result, 240 seconds)

      assert(vocab.unique == 15)
      assert(vocab.terms.contains(TermCount("or", 1)))
      assert(vocab.terms.contains(TermCount(",", 3)))
      assert(vocab.terms.contains(TermCount("more", 1)))
      assert(vocab.terms.contains(TermCount("accurately", 1)))
      assert(vocab.terms.contains(TermCount("how", 1)))
      assert(vocab.terms.contains(TermCount("do", 1)))
      assert(vocab.terms.contains(TermCount("i", 1)))
      assert(vocab.terms.contains(TermCount("convert", 1)))
      assert(vocab.terms.contains(TermCount("any", 1)))
      assert(vocab.terms.contains(TermCount("scala", 1)))
      assert(vocab.terms.contains(TermCount("sequence", 1)))
      assert(vocab.terms.contains(TermCount("to", 1)))
      assert(vocab.terms.contains(TermCount("a", 1)))
      assert(vocab.terms.contains(TermCount("string", 1)))
    }
  }

  "metrics" should {
    "compute statistics of the input" in {

      val metricFlow = annotator.tapMetrics

      val input = s"How can I convert a Scala array to a String? Or, more, accurately, how do I convert any Scala sequence to a String."
      val graph = Source.single(input).via(docFlow).via(sentenceFlow).via(metricFlow).toMat(Sink.head[TapMetrics])(Keep.right)
      val result:Future[TapMetrics] = graph.run()
      val metric = Await.result(result, 240 seconds)

      assert(metric.sentences == 2)
      assert(metric.tokens == 28)
      assert(metric.words == 23)
      assert(metric.characters == 114)
      assert(metric.punctuation == 5)
      assert(metric.whitespace == 21)
      assert(metric.sentWordCounts == Vector(10, 13))
      assert(metric.averageSentWordCount == 11.5)
      assert(metric.wordLengths == Vector(Vector(3, 3, 1, 7, 1, 5, 5, 2, 1, 6), Vector(2, 4, 10, 3, 2, 1, 7, 3, 5, 8, 2, 1, 6)) )
      assert(metric.averageWordLength == 3.8260869565217392)
      assert(metric.averageSentWordLength == Vector(3.4, 4.153846153846154) )
    }
  }

  "expressions" should {
    "extract expressions" in {
      val expressionFlow = annotator.tapExpressions

      val input = s"I believe you are the best player on our team. I would support you for sure."

      val graph = Source.single(input).via(docFlow).via(sentenceFlow).via(expressionFlow).toMat(Sink.head[Vector[TapExpressions]])(Keep.right)
      val result:Future[Vector[TapExpressions]] = graph.run()
      val expression = Await.result(result, 240 seconds)


      assert(expression.length == 2)
      //assert(expression(0).affect == Vector(TapExpression("believe",1,1)))
      assert(expression(0).epistemic == Vector(TapExpression("I believe",0,1)))
      assert(expression(0).modal == Vector())
      assert(expression(0).sentIdx == 0)

      //assert(expression(1).affect == Vector(TapExpression("support",2,2)),TapExpression("sure",5,5)))
      assert(expression(1).epistemic == Vector(TapExpression("you for sure",3,5)))
      assert(expression(1).modal == Vector(TapExpression("I would",0,1)))
      assert(expression(1).sentIdx == 1)
    }
  }

  "syllables" should {
    "count the number of syllable for each word" in {

      val syllableFlow = annotator.tapSyllables

      val input = s"It is nice to get something for free. That is for sure."
      val graph = Source.single(input).via(docFlow).via(sentenceFlow).via(syllableFlow).toMat(Sink.head[Vector[TapSyllables]])(Keep.right)
      val result:Future[Vector[TapSyllables]] = graph.run()
      val syllable = Await.result(result, 240 seconds)

      assert(syllable(0).avgSyllables == 9/9.toDouble)
      assert(syllable(0).counts == Vector(1,1,1,1,1,2,1,1))
      assert(syllable(0).sentIdx == 0)
      assert(syllable(1).avgSyllables == 4/5.toDouble)
      assert(syllable(1).counts == Vector(1,1,1,1))
      assert(syllable(1).sentIdx == 1)
    }
  }

  "spelling" should {
    "point out spelling error and suggest a correction" in {

      val spellingFlow = annotator.tapSpelling

      val input = s"I don’t no how to swim. Your the best player on our team."
      val graph = Source.single(input).via(docFlow).via(sentenceFlow).via(spellingFlow).toMat(Sink.head[Vector[TapSpelling]])(Keep.right)
      val result:Future[Vector[TapSpelling]] = graph.run()
      val spelling = Await.result(result, 240 seconds)

      assert(spelling(0).sentIdx == 0)

      assert(spelling(1).spelling(0).suggestions == Vector("You're"))
      assert(spelling(1).spelling(0).start == 0)
      assert(spelling(1).spelling(0).end == 4)
      assert(spelling(1).sentIdx == 1)
    }
  }

  "posStats" should {
    "provide statistics" in {

      val posStatFlow = annotator.tapPosStats

      val input = s"You're the best player on our team."
      val graph = Source.single(input).via(docFlow).via(sentenceFlow).via(posStatFlow).toMat(Sink.head[TapPosStats])(Keep.right)
      val result:Future[TapPosStats] = graph.run()
      val posStat = Await.result(result, 240 seconds)

      assert(posStat.verbNounRatio == 1/2.toDouble)
      assert(posStat.futurePastRatio == 0.0)
      assert(posStat.namedEntityWordRatio == 9/8.toDouble)
      assert(posStat.adjectiveWordRatio == 1/8.toDouble)
      assert(posStat.nounDistribution == Vector(1.0))
      assert(posStat.verbDistribution == Vector(1.0))
      assert(posStat.adjectiveDistribution == Vector(1.0))
    }
  }
}