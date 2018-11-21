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

package controllers.handlers

import com.google.inject.AbstractModule
import io.heta.tap.analysis.affectlexicon.AffectLexicon
import io.heta.tap.analysis.wordvector.WordVectorActor
//import io.heta.tap.nlp.factorie.LanguageToolActor
import play.api.Logger
import play.api.libs.concurrent.AkkaGuiceSupport

/**
  * Created by andrew@andrewresearch.net on 21/10/17.
  */

//class NlpInitialiserModule extends AbstractModule with AkkaGuiceSupport {
//
//  def configure():Unit = {
//    Logger.info("Binding LanguageToolActor")
//    bindActor[LanguageToolActor]("languagetool")
//    bindActor[AffectLexiconActor]("affectlexicon")
//    bindActor[WordVectorActor]("wordvector")
//  }
//
//}
