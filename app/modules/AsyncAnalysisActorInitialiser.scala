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

package modules

import com.google.inject.AbstractModule
import com.typesafe.scalalogging.Logger
import io.heta.tap.analysis.batch.BatchActor
import io.heta.tap.analysis.clu.CluAnnotatorActor
import play.api.libs.concurrent.AkkaGuiceSupport

class AsyncAnalysisActorInitialiser extends AbstractModule with AkkaGuiceSupport {

  val logger: Logger = Logger(this.getClass)

    override def configure():Unit = {
      logger.info("Binding BatchActor")
      bindActor[BatchActor]("batch")
      logger.info("Binding CluAnnotatorActor")
      bindActor[CluAnnotatorActor]("cluAnnotator")
      //bindActor[AffectLexiconActor]("affectlexicon")
      //bindActor[WordVectorActor]("wordvector")
    }

}
