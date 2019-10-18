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

package io.heta.tap.analysis.languagetool

import io.heta.tap.data.doc.Sentence
import io.heta.tap.data.doc.spell.{Spell, Spelling}
import org.languagetool.JLanguageTool
import org.languagetool.language.BritishEnglish
import org.languagetool.rules.RuleMatch

import scala.concurrent.Future
import scala.collection.JavaConverters._

/**
  * Performs the spelling check in BritishEnglish
  */

object Speller {

  import io.heta.tap.pipelines.materialize.PipelineContext.executor

  private val languages = new Languages

  private val langTool = new JLanguageTool(languages.brittishEnglish)

  langTool.getLanguage.isInstanceOf[BritishEnglish]

  /**
    * Checks the spellings
    *
    * @param sentences input sentence
    * @return spelling mistakes and possible suggestions for what the intended word was
    */
  def check(sentences:Vector[Sentence]): Future[Vector[Spelling]] = Future {
    sentences.map { sent =>
      val matches:Vector[RuleMatch] = langTool.check(sent.original).asScala.toVector
      val result = matches.map( m => Spell(m.getMessage,m.getSuggestedReplacements.asScala.toVector,m.getFromPos,m.getToPos))
      Spelling(sent.idx,result)
    }
  }

}
