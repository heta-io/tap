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

/*
package tap.nlp.old.openNlp

import tap.UnitSpec

/**
  * Created by andrew@andrewresearch.net on 12/7/17.
  */
class ParserSpec extends UnitSpec {

  val input = "My first lecture started 13 March 2015. This is another sentence."
  val sentences = List("My first lecture started 13 March 2015.","This is another sentence.")
  val tokens = List("My", "first", "lecture", "started", "13", "March", "2015", ".")
  val posTags = List("PRP$", "JJ", "NN", "VBD", "CD", "NNP", "CD", ".")
  val constTree = "(TOP (S (NP (PRP$ My) (JJ first) (NN lecture)) (VP (VBD started) (NP (CD 13) (NNP March) (CD 2015))) (. .)))"

  "sentence" should "parse text into sentences" in {
    val res = Parsers.sentence(input)
    assert(res==sentences)
  }

  "token" should "parse a sentence into tokens" in {
    val res = Parsers.token(sentences.head)
    assert(res==tokens)
  }

  "postag" should "return postags from a list of tokens" in {
    val res = Parsers.posTag(tokens)
    assert(res==posTags)
  }

  "parseTree" should "parse a sentence into tree output as a string" in {
    val res = Parsers.parseTree(tokens.mkString(" "))
    println(res)
    assert(res==constTree)
  }

}
*/