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

package au.edu.utscic.tap.pipelines

import akka.NotUsed
import akka.stream.FlowShape
import akka.stream.scaladsl.{Flow, GraphDSL, Merge, Source}
import au.edu.utscic.tap.data._
import au.edu.utscic.tap.nlp.factorie.Annotator
import cc.factorie.app.nlp.Document

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 6/9/17.
  */
object Parsing {

  object Pipeline {
    val sentences: Flow[String, List[TapSentence], NotUsed] = makeDocument via tapSentences
    val vocab: Flow[String, TapVocab, NotUsed] = makeDocument via tapSentences via tapVocab
    val metrics: Flow[String, TapMetrics, NotUsed] = makeDocument via tapSentences via tapMetrics
    val expressions: Flow[String, List[TapExpressions], NotUsed] = makeDocument via tapSentences via tapExpressions
  }

  val makeDocument: Flow[String, Document, NotUsed] = Flow[String].map(str => Annotator.document(str))

  val tapSentences: Flow[Document, List[TapSentence], NotUsed] =
    Flow[Document]
      .map(doc => Annotator.sentences(doc))
      .map(sentList => Annotator.tapSentences(sentList))

  val tapVocab: Flow[List[TapSentence], TapVocab, NotUsed] =
    Flow[List[TapSentence]]
      .map { lst =>
        lst.flatMap(_.tokens)
          .map(_.term.toLowerCase)
          .groupBy((term: String) => term)
          .mapValues(_.length)
      }.map { m =>
      val lst: List[TermCount] = m.toList.map { case (k, v) => TermCount(k, v) }
      TapVocab(m.size, lst)
    }

  val tapMetrics: Flow[List[TapSentence], TapMetrics, NotUsed] =
    Flow[List[TapSentence]]
      .map { lst =>
        lst.map(_.length)
      }.map(lst => TapMetrics(lst.sum))

  val tapExpressions: Flow[List[TapSentence], List[TapExpressions], NotUsed] =
    Flow[List[TapSentence]].mapAsync[List[TapExpressions]](3) { lst =>
      val results = lst.map { sent =>
        for {
            ae <- Expressions.affect(sent.tokens)
            ee <- Expressions.epistemic(sent.tokens)
            me <- Expressions.modal(sent.tokens)
        } yield (TapExpressions(ae, ee, me, sent.idx))
      }
      Future.sequence(results)
    }

//      .map { lst =>
//        lst.mapAsync[TapExpressions](3) { sent =>
//          val allTokens: Vector[TapToken] = sent.tokens
//          for {
//            ae <- Expressions.affect(allTokens)
//            ee <- Expressions.epistemic(allTokens)
//            me <- Expressions.modal(allTokens)
//
//          } yield (TapExpressions(ae, ee, me))
//        }
//      }
}

/*
object Vocab {

  val document:Flow[OldTapDocument,List[OldTapSection],NotUsed] = Flow[OldTapDocument].map(_.sections)

  val sectionsVocab:Flow[List[OldTapSection],List[Map[String,Int]],NotUsed] = Flow[List[OldTapSection]].map(_.map(_.sentences.flatMap(_.tokens).groupBy((word:String) => word).mapValues(_.length)))
  val documentVocab = Flow[List[Map[String,Int]]].fold(Map[String,Int]())(_ ++ _.flatten)

  val vocabByCount = Flow[Map[String,Int]].map(_.toList.groupBy(_._2).map(wc => wc._1 -> wc._2.map(_._1)).toSeq.reverse).map(l=> ListMap(l:_*))

  val pipeline = document.via(sectionsVocab).via(documentVocab).via(vocabByCount)

}
*/