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

package tap.analysis.clu

import org.clulab.processors.Document
import org.clulab.processors.clu.CluProcessor
import play.api.Logger

class CluAnnotator {

  private val logger: Logger = Logger(this.getClass)

  val processor = new CluProcessor()

  this.init()

  private def init():Unit = {
    logger.info("Initialising CluProcessor")
    val text = """CluProcessor is starting up!"""
    val aDoc = this.annotate(text)
    logger.info("Initialised CluProcessor >> "+aDoc.sentences.length)
  }

  def annotate(text:String):Document = {
    val doc = processor.mkDocument(text)
    processor.annotate(doc)
  }
}
