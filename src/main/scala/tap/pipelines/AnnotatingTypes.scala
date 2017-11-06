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
import akka.stream.scaladsl.Flow
import io.nlytx.nlp.api.DocumentModel.{Document, Section}
import tap.data._ // scalastyle:ignore

/**
  * Created by andrew@andrewresearch.net on 6/11/17.
  */

object AnnotatingTypes {
  /* pipetype values */
  val STANDARD = "standard"
  val FAST = "fast"
  val NER = "ner"
  val DEFAULT = FAST
  def validPipeType(pipetype:Option[String]):String = {
    val pt = pipetype.getOrElse(DEFAULT).toLowerCase
    if(List(STANDARD,FAST,NER).contains(pt)) pt else DEFAULT
  }

  /* Some convenience types */
  type TapSentences = Vector[TapSentence]
  type Sections = Vector[Section]

  type DocumentFlow = Flow[String, Document, NotUsed]
  type SentencesFlow = Flow[Document, TapSentences, NotUsed]
  type VocabFlow = Flow[Document, TapVocab, NotUsed]
  type MetricsFlow = Flow[Document, TapMetrics, NotUsed]
  type ExpressionsFlow = Flow[Document, Vector[TapExpressions], NotUsed]
  type SyllablesFlow = Flow[Document, Vector[TapSyllables],NotUsed]
  type SpellingFlow = Flow[Document, Vector[TapSpelling],NotUsed]
  type PosStatsFlow = Flow[Document, TapPosStats, NotUsed]
}
