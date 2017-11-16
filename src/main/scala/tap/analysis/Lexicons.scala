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

package tap.analysis

import play.api.Logger

//import org.json4s.{DefaultFormats, Formats, Serialization, jackson}
//import org.json4s.jackson

/**
  * Created by andrew@andrewresearch.net on 16/10/17.
  */
object Lexicons {

  val logger: Logger = Logger(this.getClass)

  def matchEpistemicVerbs(terms:Vector[String],useLemmas:Boolean = false):Vector[String] = terms
    .intersect(if (useLemmas) epistemicVerbLemmas else epistemicVerbTerms)

  type Lexicon = Vector[String]

  val epistemicVerbTerms:Lexicon = Vector("think","thought","believe","believed","guess","guessed","suppose","supposed",
    "sure","certain","confident","learnt","learned","imagine","imagined","wonder","wondered","consider","considered",
    "realise","realised","realize","realized","understand","understood","assume","assumed","admit")
  val epistemicVerbLemmas:Lexicon = Vector("think","believe","guess","suppose","sure","certain","confident",
    "learnt","learn","imagine","wonder","consider","realise","realize","understand","assume","admit")
}