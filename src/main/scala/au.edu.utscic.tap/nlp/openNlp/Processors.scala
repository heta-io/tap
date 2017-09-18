package au.edu.utscic.tap.nlp.openNlp

import au.edu.utscic.tap.nlp.{NlpDocument, OldNlpSentence}


/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */
object Processors {
  def textToDoc(text:String):NlpDocument = {
    val sentences = Parsers.sentence(text).map(textToSentence(_)) //List(textToSentence("This has"),textToSentence("been converted"))
    NlpDocument(text,sentences)
  }
  def textToSentence(text:String):OldNlpSentence = {
    val words = Parsers.token(text)
    val posTags = Parsers.posTag(words)
    val lemmas = List()//Parsers.lemma(words,posTags)
    OldNlpSentence(text,words,lemmas,posTags)
  }
}
