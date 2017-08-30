package au.edu.utscic.tap.nlp

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */
trait SentenceConverter[T] {
  def fromText(text:String): NlpSentence
}
