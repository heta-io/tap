package au.edu.utscic.tap.nlp.nlp4j

import java.io.InputStream
import java.util

//import edu.emory.mathcs.nlp.common.util.StringUtils
//import edu.emory
//import edu.emory.mathcs.nlp.component.dep.DEPParser
//import edu.emory.mathcs.nlp.component.template.feature.Field
//import edu.emory.mathcs.nlp.component.template.{NLPComponent, OnlineComponent}
//import edu.emory.mathcs.nlp.component.template.node.NLPNode
//import edu.emory.mathcs.nlp.component.tokenizer.{EnglishTokenizer, Tokenizer}
//import edu.emory.mathcs.nlp.component.tokenizer.token.{Token, TokenIndex}
//import edu.emory.mathcs.nlp.decode.NLPDecoder
//import edu.emory.mathcs.nlp.models._

/**
  * Created by andrew@andrewresearch.net on 12/7/17.
  */
object DependencyParser {

//  //lazy val depParser = new edu.emory.mathcs.nlp.component.dep.DEPParser[]()
//  val configuration:InputStream = getClass.getResourceAsStream("/nlp4j-dep-config.xml")
//  //val model:Array[Byte] = ???
//  lazy val depParser = new DEPParser[NLPNode](configuration)
//  lazy val compList:util.List[NLPComponent[NLPNode]] = new util.ArrayList[NLPComponent[NLPNode]]()
//  compList.add(depParser)
//  lazy val tokenizer = new EnglishTokenizer()
//  lazy val decoder = new NLPDecoder()
//  decoder.setTokenizer(tokenizer)
//  decoder.setComponents(compList)
//
//
//  def process(text:String) = {
//    val nodes = decoder.decode(text)
//      nodes.toList.map( node => (node.getWordForm,node.getID,node.getValue(Field.word_shape)))
//  }

//  def process(tokens:List[String],posTags:List[String]):List[String] = {
//
//    tokens zip posTags map { case (token,posTag) =>
//      //lemmatizer.lemmatize(StringUtils.toSimplifiedForm(token),posTag)
//      decoder.decode("This is a test")
//    }
//
//  }
}
