package au.edu.utscic.tap.nlp.openNlp

import au.edu.utscic.tap.nlp.{NlpDocument, NlpSentence}


/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */
object Processors {
  def textToDoc(text:String):NlpDocument = {
    val sentences = Parsers.sentence(text).map(textToSentence(_)) //List(textToSentence("This has"),textToSentence("been converted"))
    NlpDocument(text,sentences)
  }
  def textToSentence(text:String):NlpSentence = {
    val words = Parsers.token(text)
    val posTags = Parsers.posTag(words)
    val lemmas = List()//Parsers.lemma(words,posTags)
    NlpSentence(text,words,lemmas,posTags)
  }
}
