package au.edu.utscic.tap.data.nlp

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */
trait DocumentConverter[T]{
  def fromText(text:String): NlpDocument
}
