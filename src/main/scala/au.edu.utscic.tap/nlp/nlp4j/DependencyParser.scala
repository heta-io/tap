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
//import edu.emory.mathcs.nlp.component.tokenizer.token.{TapToken, TokenIndex}
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
