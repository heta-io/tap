/*
 * Copyright (c) 2016-2018 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 */

///*
// * Copyright (c) 2016-2018 original author or authors
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * You may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software distributed under
// * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
// * language governing permissions and limitations under the License.
// *
// */
//
//package io.heta.tap.nlp.factorie
//
//import akka.actor.Actor
//import com.typesafe.scalalogging.Logger
//import org.languagetool.JLanguageTool
//import org.languagetool.language.BritishEnglish
//import org.languagetool.rules.RuleMatch
//import io.heta.tap.analysis.languagetool.Languages
//import io.heta.tap.data.doc.spell.Spell
//import io.heta.tap.nlp.factorie.LanguageToolActor.{CheckSpelling, INIT}
//
//import scala.collection.JavaConverters._
//
///**
//  * Created by andrew@andrewresearch.net on 21/10/17.
//  */
//
//object LanguageToolActor {
//  object INIT
//  case class CheckSpelling(text:String)
//}
//
//class LanguageToolActor extends Actor {
//
//  val languages: Languages = new Languages()
//
//  val logger: Logger = Logger(this.getClass)
//
//  def receive: PartialFunction[Any,Unit] = {
//    case INIT => sender ! init
//    case cs:CheckSpelling => sender ! check(cs.text)
//    case msg:Any => {
//      logger.error(s"LanguageToolAnnotatorActor received unkown msg: $msg")
//    }
//  }
//
//  val langTool = new JLanguageTool(languages.brittishEnglish)
//
//  def init:Boolean = {
//    //TODO comment in to use statistical ngram data:
//    //langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
//    langTool.getLanguage.isInstanceOf[BritishEnglish]
//  }
//
//  def check(text:String):Vector[Spell] = {
//    val matches:Vector[RuleMatch] = langTool.check(text).asScala.toVector
//    matches.map( m => Spell(m.getMessage,m.getSuggestedReplacements.asScala.toVector,m.getFromPos,m.getToPos))
//  }
//
//}
