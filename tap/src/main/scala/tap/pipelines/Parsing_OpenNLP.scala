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
import akka.stream.scaladsl.{Flow, Framing, Source}
import akka.util.ByteString
import tap.data.CustomTypes.{DocumentStr, SectionStr}
import tap.data.{OldTapDocument, OldTapSection, OldTapSentence, OldTapTags}
//import tap.nlp.old.{NlpBuilders, NlpDocument, OldNlpSentence}

/**
  * Created by andrew@andrewresearch.net on 24/2/17.
  */
object Parsing_OpenNLP {

  object Pipeline {
    /** A stream of document strings split into sections on newline,
      *  which are treated as NLP Documents and then used to produce
      *  TapSections that are folded into a OldTapDocument
      */
    //val default:Flow[SectionStr,OldTapDocument,NotUsed] = nlpDocs.via(nlpSentences).via(tapSentences).via(tapSection).via(tapDocument)


  }

  //import tap.nlp.old.openNlp.OpenNlpImplicits._




/*
  val nlpDocs:Flow[SectionStr,NlpDocument,NotUsed] = Flow[String].map(s => NlpBuilders.document(s))

  val nlpSentences:Flow[NlpDocument,List[OldNlpSentence],NotUsed] = Flow[NlpDocument].map(_.sentences)

  val tapSentences:Flow[List[OldNlpSentence],List[OldTapSentence],NotUsed] = Flow[List[OldNlpSentence]].map(_.map(s => buildTapSentence(s)))

  val tapSection:Flow[List[OldTapSentence],OldTapSection,NotUsed] = Flow[List[OldTapSentence]].map(OldTapSection(_))

  val tapDocument = Flow[OldTapSection].fold(List[OldTapSection]())(_:+_).map(OldTapDocument(_))



  def buildTapSentence(sentence:OldNlpSentence) = OldTapSentence(
    sentence.text,
    sentence.words.map(_.toLowerCase),
    OldTapTags(
      sentence.lemmas,
      sentence.posTags
    )
  )
*/

}


