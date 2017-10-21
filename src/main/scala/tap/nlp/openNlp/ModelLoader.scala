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

import java.io.{FileInputStream, IOException}

import opennlp.tools.lemmatizer.LemmatizerModel
import opennlp.tools.parser.{ParserChunkerFactory, ParserFactory, ParserModel}
import opennlp.tools.postag.POSModel
import opennlp.tools.sentdetect.SentenceModel
import opennlp.tools.tokenize.TokenizerModel


object ModelLoader {

  def load[T](t:Class[T]):Option[T] = {
    try {
      val model = t match {
        case Models.Sentence.kind => new SentenceModel(modelFile(Models.Sentence.path))
        case Models.Token.kind => new TokenizerModel(modelFile(Models.Token.path))
        case Models.PosTag.kind => new POSModel(modelFile(Models.PosTag.path))
        case Models.Lemma.kind => new LemmatizerModel(modelFile(Models.Lemma.path))
        case Models.ChunkingParser.kind => new ParserModel(modelFile(Models.ChunkingParser.path))
      }
      Some(model.asInstanceOf[T])
    }
    catch {
      case e: IOException => {
        println("The model loader was unable to find a matching model:")
        //e.printStackTrace()
        None
      }

    }
  }

  private def modelFile(model:String) = {
    println("Loading models from: "+(new java.io.File(model).getCanonicalPath))
    new FileInputStream(model)
  }


}
