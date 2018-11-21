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
import io.heta.tap.data.doc.expression.affect.AffectThresholds
import io.heta.tap.pipelines.materialize.FilePipeline.File
import org.clulab.processors.Document

/*
These Pipes are connected Flow Segments ready to be deployed in a Pipeline
 */
object Pipe {

  val annotatedSentences: Flow[Document, File, NotUsed] =
    Segment.Document_SentencesBatchResult via
      Segment.AnalyticsResult_File

  val vocabulary: Flow[Document, File, NotUsed] =
    Segment.Document_SentencesBatchResult via
      Segment.Sentences_Vocabulary via
        Segment.AnalyticsResult_File

  val metrics: Flow[Document, File, NotUsed] =
    Segment.Document_SentencesBatchResult via
      Segment.Sentences_Metrics via
        Segment.AnalyticsResult_File

  val posStats: Flow[Document, File, NotUsed] =
    Segment.Document_SentencesBatchResult via
      Segment.Sentences_PosStats via
        Segment.AnalyticsResult_File

  val syllables: Flow[Document, File, NotUsed] =
    Segment.Document_SentencesBatchResult via
      Segment.Sentences_Syllables via
        Segment.AnalyticsResult_File

  val spelling: Flow[Document, File, NotUsed] =
    Segment.Document_SentencesBatchResult via
      Segment.Sentences_Spelling via
        Segment.AnalyticsResult_File

  val expressions: Flow[Document, File, NotUsed] =
    Segment.Document_SentencesBatchResult via
      Segment.Sentences_Expressions via
        Segment.AnalyticsResult_File

  val affectExpressions: Flow[Document, File, NotUsed] =
    Segment.Document_SentencesBatchResult via
      Segment.Sentences_AffectExpressions(Some(AffectThresholds(arousal=0.0,valence = 0.0,dominance = 0.0))) via
        Segment.AnalyticsResult_File

  val reflectExpressions: Flow[Document, File, NotUsed] =
    Segment.Document_ReflectiveExpressionsBatchResult via
      Segment.AnalyticsResult_File

}
