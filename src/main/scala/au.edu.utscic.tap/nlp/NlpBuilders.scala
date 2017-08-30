package au.edu.utscic.tap.nlp

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */
object NlpBuilders {
  def document[T](text: String)(implicit toDoc: DocumentConverter[T]): NlpDocument = {
    toDoc.fromText(text)
  }
  def sentence[T](text: String)(implicit toSentence: SentenceConverter[T]): NlpSentence = {
    toSentence.fromText(text)
  }
}
