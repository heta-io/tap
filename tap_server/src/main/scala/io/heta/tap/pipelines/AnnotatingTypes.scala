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
import io.nlytx.expressions.data.ReflectiveExpressions
import io.nlytx.nlp.api.DocumentModel.{Document, Section}
import org.clulab.processors
import io.heta.tap.data._
import io.heta.tap.data.doc.expression.reflect.ReflectExpressions
import io.heta.tap.data.doc.spell.Spelling
import io.heta.tap.data.doc.{Metrics, Sentence, PosStats} // scalastyle:ignore

/**
  * Created by andrew@andrewresearch.net on 6/11/17.
  */

object AnnotatingTypes {
  /* pipetype values */
  val STANDARD = "standard"
  val FAST = "fast"
  val NER = "ner"
  val CLU = "clu"
  val DEFAULT = FAST


  /* Some convenience types */
  type TapSentences = Vector[Sentence]
  type Sections = Vector[Section]

  //type CluDocumentFlow = Flow[String,processors.Document, NotUsed]
  //type CluSentencesFlow = Flow[processors.Document, TapSentences, NotUsed]
  type DocumentFlow = Flow[String, Document, NotUsed]
  //type SentencesFlow = Flow[Document, TapSentences, NotUsed]
  //type VocabFlow = Flow[Document, TapVocab, NotUsed]
  type MetricsFlow = Flow[Document, Metrics, NotUsed]
  //type ExpressionsFlow = Flow[Document, Vector[Expressions], NotUsed]
  type SyllablesFlow = Flow[Document, Vector[TapSyllables],NotUsed]
  type SpellingFlow = Flow[Document, Vector[Spelling],NotUsed]
  type PosStatsFlow = Flow[Document, PosStats, NotUsed]
  type ReflectExpressionFlow = Flow[Document, ReflectExpressions, NotUsed]
  //type AffectExpressionFlow = Flow[processors.Document, Vector[AffectExpressions], NotUsed]


}
