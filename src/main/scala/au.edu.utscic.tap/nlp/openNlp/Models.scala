package au.edu.utscic.tap.nlp.openNlp

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
