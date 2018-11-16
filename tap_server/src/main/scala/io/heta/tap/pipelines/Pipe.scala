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
import io.heta.tap.pipelines.materialize.FilePipeline.File
import org.clulab.processors.Document

/*
These Pipes are connected Flow Segments ready to be deployed in a Pipeline
 */
object Pipe {

  val cluSentences: Flow[Document, File, NotUsed] =
    Segment.cluTapSentences via
      Segment.FileFromAnalyticsResult

  val affectExpressions: Flow[Document, File, NotUsed] =
    Segment.cluTapSentences via
      Segment.affectExpressions(None) via
        Segment.FileFromAnalyticsResult


}
