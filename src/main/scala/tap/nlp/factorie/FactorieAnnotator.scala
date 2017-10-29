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

package tap.nlp.factorie

import javax.inject.Singleton

import cc.factorie.app.nlp.lexicon.{LexiconsProvider, StaticLexicons}
import cc.factorie.app.nlp.ner.{ConllChainNer, StaticLexiconFeatures}
import cc.factorie.app.nlp._
import cc.factorie.util.{ClasspathURL, ModelProvider}
import io.nlytx.factorie.nlp.api.DocumentBuilder
import play.api.Logger.logger

/**
  * Created by andrew@andrewresearch.net on 21/10/17.
  */

@Singleton
class FactorieAnnotator {
  logger.info("Initialising Factorie")
  val db = new DocumentBuilder

  def process(text:String):Document = db.createAnnotatedDoc(text)
}

//  val mp = ModelProvider.classpath[ConllChainNer]()
//  val staticLexiconFeatures = new StaticLexiconFeatures(new StaticLexicons()(LexiconsProvider.classpath()), "en")
//  val conllChainNer = new ConllChainNer()(mp, staticLexiconFeatures)
//
//  val path = ClasspathURL[coref.NerForwardCoref](".factorie").getPath
//  System.setProperty(classOf[coref.NerForwardCoref].getName,path)
//


//  val default = DocumentAnnotatorPipeline(
//    pos.OntonotesForwardPosTagger,
//    parse.WSJTransitionBasedParser,
//    conllChainNer,
//    phrase.PosBasedNounPhraseFinder,
//    //coref.NerForwardCoref
//  )

