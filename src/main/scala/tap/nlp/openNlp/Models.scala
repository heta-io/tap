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

import opennlp.tools.lemmatizer.LemmatizerModel
import opennlp.tools.parser.ParserModel
import opennlp.tools.postag.POSModel
import opennlp.tools.sentdetect.SentenceModel
import opennlp.tools.tokenize.TokenizerModel

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */
object Models {
  def fullPath(model:String) = "./models/opennlp/"+model

  object Sentence {
    val path = fullPath("en-sent.bin")
    val kind = classOf[SentenceModel]
  }

  object Token {
    val path = fullPath("en-token.bin")
    val kind = classOf[TokenizerModel]
  }

  object PosTag {
    val path = fullPath("en-pos-maxent.bin")
    val kind = classOf[POSModel]
  }

  object Lemma {
    //val path = fullPath("en-lemma-perceptron-conll09.bin")
    val path = fullPath("en-lemmatizer.dic.txt")
    val kind = classOf[LemmatizerModel]
  }

  object ChunkingParser {
    val path = fullPath("en-parser-chunking.bin")
    val kind = classOf[ParserModel]
  }
}
