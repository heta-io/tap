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

  "annotator" should {
    val docFlow = annotator.makeDocument

    "make a valid document flow" in {

      assert(docFlow.isInstanceOf[Flow[String,Document,NotUsed]])
    }

    "materialize the flow" in {
      import tap.pipelines.materialize.PipelineContext.materializer
      //val docFlow = annotator.makeDocument
      val graph = Source.single("This is a test.").via(docFlow).toMat(Sink.head[Document])(Keep.right)
      val result:Future[Document] = graph.run()
      val doc = Await.result(result, 240 seconds)
      assert(doc.tokenCount==5)
    }
  }


//
//  def testSource(input:String) = Source.single(input)
//
//  "sentences" should {
//    "extract separated sentences" in {
//
//      val testSentenceSink = Flow[List[TapSentence]].toMat(Sink.head[List[TapSentence]])(Keep.right)
//
//      val input = s"How can I convert a Scala array to a String? Or, more, accurately, how do I convert any Scala sequence to a String."
//      val future = testSource(input) via annotator.Pipeline.sentences runWith testSentenceSink
//      val result = Await.result(future, 180 seconds)
//
//      assert(result.length == 2)
//      assert(result(0).original == "How can I convert a Scala array to a String?")
//      assert(result(1).original == "Or, more, accurately, how do I convert any Scala sequence to a String.")
//    }
//  }
}

/*
class AnnotatingPipelineSpec extends AsyncFlatSpec {
//
//  private val system = ActorSystem("mySystem")
//  private val factorieAnnotatorTest = system.actorOf(Props[FactorieAnnotatorActor], "factorie-annotator-test")
//
//
//
//  def getDoc:Future[Document] = {
//    implicit val timeout:Timeout = 120.seconds
//    ask(factorieAnnotatorTest,TestDocument("This is a test.")).mapTo[Future[Document]].flatMap(identity(_))
//  }
//
//  "factorieAnnotatorActor" should "return a valid document" in {
//    getDoc map { doc => assert(doc.tokenCount == 5) }
//  }

  import tap.pipelines.materialize.PipelineContext._

  //dependency injection
  private val app = new GuiceApplicationBuilder().build
  private val annotator = app.injector.instanceOf[Annotating]




  "vocab" should "extract vocabularies and their frequency" in {

    val testVocabSink = Flow[TapVocab].toMat(Sink.head[TapVocab])(Keep.right)

    val input = s"Or, more, accurately, how do I convert any Scala sequence to a String."
    val future = testSource(input) via annotator.Pipeline.vocab runWith testVocabSink
    val result = Await.result(future, 10 seconds)

    assert(result.unique == 15)
    assert(result.terms.contains(TermCount("or", 1)))
    assert(result.terms.contains(TermCount(",", 3)))
    assert(result.terms.contains(TermCount("more", 1)))
    assert(result.terms.contains(TermCount("accurately", 1)))
    assert(result.terms.contains(TermCount("how", 1)))
    assert(result.terms.contains(TermCount("do", 1)))
    assert(result.terms.contains(TermCount("i", 1)))
    assert(result.terms.contains(TermCount("convert", 1)))
    assert(result.terms.contains(TermCount("any", 1)))
    assert(result.terms.contains(TermCount("scala", 1)))
    assert(result.terms.contains(TermCount("sequence", 1)))
    assert(result.terms.contains(TermCount("to", 1)))
    assert(result.terms.contains(TermCount("a", 1)))
    assert(result.terms.contains(TermCount("string", 1)))
  }

  "metrics" should "compute statistics of the input" in {

    val testMetricSink = Flow[TapMetrics].toMat(Sink.head[TapMetrics])(Keep.right)

    val input = s"How can I convert a Scala array to a String? Or, more, accurately, how do I convert any Scala sequence to a String."
    val future = testSource(input) via annotator.Pipeline.metrics runWith testMetricSink
    val result = Await.result(future, 10 seconds)

    assert(result.sentences == 2)
    assert(result.tokens == 28)
    assert(result.words == 23)
    assert(result.characters == 114)
    assert(result.punctuation == 5)
    assert(result.whitespace == 21)
    assert(result.sentWordCounts == Vector(10, 13))
    assert(result.averageSentWordCount == 11.5)
    assert(result.wordLengths == Vector(Vector(3, 3, 1, 7, 1, 5, 5, 2, 1, 6), Vector(2, 4, 10, 3, 2, 1, 7, 3, 5, 8, 2, 1, 6)) )
    assert(result.averageWordLength == 3.8260869565217392)
    assert(result.averageSentWordLength == Vector(3.4, 4.153846153846154) )
  }

  "expressions" should "extract expressions" in {

    val testExpressionSink = Flow[List[TapExpressions]].toMat(Sink.head[List[TapExpressions]])(Keep.right)

    val input = s"I believe you are the best player on our team. I would support you for sure."
    val future = testSource(input) via annotator.Pipeline.expressions runWith testExpressionSink
    val result = Await.result(future, 10 seconds)

    assert(result.length == 2)
    assert(result(0).affect == Vector(TapExpression("believe",1,1)))
    assert(result(0).epistemic == Vector(TapExpression("I believe",0,1)))
    assert(result(0).modal == Vector())
    assert(result(0).sentIdx == 0)

    assert(result(1).affect == Vector())
    assert(result(1).epistemic == Vector(TapExpression("you for sure",3,5)))
    assert(result(1).modal == Vector(TapExpression("I would",0,1)))
    assert(result(1).sentIdx == 1)
  }

  "syllables" should "count the number of syllable for each word" in {

    val testSyllableSink = Flow[List[TapSyllables]].toMat(Sink.head[List[TapSyllables]])(Keep.right)

    val input = s"It is nice to get something for free. That is for sure."
    val future = testSource(input) via annotator.Pipeline.syllables runWith testSyllableSink
    val result = Await.result(future, 10 seconds)

    assert(result(0).avgSyllables == 9/9.toDouble)
    assert(result(0).counts == Vector(1,1,1,1,1,2,1,1))
    assert(result(0).sentIdx == 0)
    assert(result(1).avgSyllables == 4/5.toDouble)
    assert(result(1).counts == Vector(1,1,1,1))
    assert(result(1).sentIdx == 1)
  }

  "spelling" should "point out spelling error and suggest a correction" in {

    val testSpellingSink = Flow[List[TapSpelling]].toMat(Sink.head[List[TapSpelling]])(Keep.right)

    val input = s"I don’t no how to swim. Your the best player on our team."
    val future = testSource(input) via annotator.Pipeline.spelling runWith testSpellingSink
    val result = Await.result(future, 10 seconds)

    assert(result(0).sentIdx == 0)

    assert(result(1).spelling(0).suggestions == Vector("You're"))
    assert(result(1).spelling(0).start == 0)
    assert(result(1).spelling(0).end == 4)
    assert(result(1).sentIdx == 1)
  }

  "posStats" should "provide statistics" in {

    val testPosStatSink = Flow[TapPosStats].toMat(Sink.head[TapPosStats])(Keep.right)

    val input = s"You're the best player on our team."
    val future = testSource(input) via annotator.Pipeline.posStats runWith testPosStatSink
    val result = Await.result(future, 10 seconds)

    assert(result.verbNounRatio == 1/2.toDouble)
    assert(result.futurePastRatio == 0.0)
    assert(result.namedEntityWordRatio == 0/8.toDouble)
    assert(result.adjectiveWordRatio == 1/8.toDouble)
    assert(result.nounDistribution == Vector(1.0))
    assert(result.verbDistribution == Vector(1.0))
    assert(result.adjectiveDistribution == Vector(1.0))
  }

}
*/