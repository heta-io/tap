package au.edu.utscic.tap.nlp.openNlp

import au.edu.utscic.tap.nlp.{NlpDocument, DocumentConverter, NlpSentence, SentenceConverter}

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */
object OpenNlpImplicits {
  implicit object OpenNlpToDocument extends DocumentConverter[NlpDocument] {
    def fromText(text:String):NlpDocument = Processors.textToDoc(text)
  }
  implicit object OpenNlpToSentence extends SentenceConverter[NlpSentence] {
    def fromText(text:String) = Processors.textToSentence(text)
  }
}
