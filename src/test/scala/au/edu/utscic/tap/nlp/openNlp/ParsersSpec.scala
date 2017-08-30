package au.edu.utscic.tap.nlp.openNlp

import au.edu.utscic.tap.UnitSpec

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
