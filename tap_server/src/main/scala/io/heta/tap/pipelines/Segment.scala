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
import akka.util.ByteString
import com.typesafe.scalalogging.Logger
import io.heta.tap.data.{CluTapSentences, SentsAnalytics, TapSentence, TapToken}
import io.heta.tap.pipelines.materialize.FilePipeline.File
import org.clulab.processors.Document
import play.api.libs.json.Json

/*
These are Flow Segments that are joined together to make Pipes
 */
object Segment {

  private val logger: Logger = Logger(this.getClass)

  val cluTapSentences: Flow[Document, CluTapSentences, NotUsed] = Flow[org.clulab.processors.Document]
    .map { doc =>
      logger.info("Extracting sentences")
      val sents = doc.sentences.toList.zipWithIndex.map { case (s, idx) =>
        val tokens = getTokens(s.startOffsets,s.words,s.lemmas,s.tags,s.entities)
        TapSentence(s.getSentenceText,tokens,-1,-1,s.words.length,idx)
      }.toVector
      CluTapSentences(doc.id.getOrElse("unknown"),sents)
    }

  val FileFromTapSentences: Flow[CluTapSentences, File, NotUsed] = Flow[CluTapSentences].map[File] { cts =>
    val analytics = SentsAnalytics(cts).asJson
    File(cts.name,ByteString(Json.prettyPrint(analytics)))
  }

  private def getTokens(start:Array[Int],words:Array[String],
                        lemmas:Option[Array[String]],posTags:Option[Array[String]],nerTags:Option[Array[String]]) = {
    val numTokens = words.length
    val is = List.range(0, numTokens)
    val ws = words.toVector
    logger.info(words.mkString("|"))
    val ls = lemmas.map(_.toVector).getOrElse(Vector.fill(numTokens)(""))
    val pts = posTags.map(_.toVector).getOrElse(Vector.fill(numTokens)(""))
    logger.info(pts.mkString("|"))
    val nts = nerTags.map(_.toVector).getOrElse(Vector.fill(numTokens)(""))

    val tapTokens = for {
      ((((i,w),l),pt),nt) <- is zip ws zip ls zip pts zip nts
    } yield TapToken(i,w,l,pt,nt,-1,Vector(),"",false)

    tapTokens.toVector
  }



}
