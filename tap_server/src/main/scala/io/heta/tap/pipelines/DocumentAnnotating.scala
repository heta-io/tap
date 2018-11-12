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

import akka.stream.scaladsl.Flow
import akka.util.ByteString
import io.heta.tap.analysis.clu.CluAnnotator
import io.heta.tap.pipelines.materialize.FilePipeline.File
import org.clulab.processors.Document

class DocumentAnnotating {

  object Pipeline {
    val cluDoc = CluDocFromFile via FileFromCluDoc
  }

  val ca:CluAnnotator = new CluAnnotator()

  val CluDocFromFile = Flow[File]
    .map[org.clulab.processors.Document] { file =>
    val doc = ca.annotate(file.contents.utf8String)
    doc.id = Some(file.name)
    doc
  }

  val FileFromCluDoc = Flow[Document].map[File] { doc =>
    val name = doc.id.getOrElse("No Name")
    val tree = doc.sentences.toList.map(_.tags.getOrElse(Array()).mkString(",")).mkString("|")
    File(name,ByteString(tree))
  }

}
