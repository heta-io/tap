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

package tap.nlp.openNlp

import tap.nlp.{NlpDocument, OldNlpSentence}


/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */
object Processors {
  def textToDoc(text:String):NlpDocument = {
    val sentences = Parsers.sentence(text).map(textToSentence(_)) //List(textToSentence("This has"),textToSentence("been converted"))
    NlpDocument(text,sentences)
  }
  def textToSentence(text:String):OldNlpSentence = {
    val words = Parsers.token(text)
    val posTags = Parsers.posTag(words)
    val lemmas = List()//Parsers.lemma(words,posTags)
    OldNlpSentence(text,words,lemmas,posTags)
  }
}
