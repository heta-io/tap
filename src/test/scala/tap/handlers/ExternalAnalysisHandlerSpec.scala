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

package tap.handlers


import handlers.ExternalAnalysisHandler
import org.scalatestplus.play.PlaySpec
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.Await
import scala.concurrent.duration._

class ExternalAnalysisHandlerSpec extends PlaySpec {
  //dependency injection
  private val app = new GuiceApplicationBuilder().build
  private val analysisHandler = app.injector.instanceOf[ExternalAnalysisHandler]

  "analysisHandler" should {
    "connect to Athanor Server" in {
      val analysis = analysisHandler.analyseWithAthanor("This is a test", Some(""))
      val result = Await.result(analysis, 240 seconds)
      assert(result.message == "ok")
    }
  }

  "RhetoricalMoves" should {
    "query RhetoricalMoves" in {
      val input = "It didn't take any time for Dr. Smith to review the subject outline by logging onto UTS Online. However, I walked into class like a blank canvas."
      val analysis = analysisHandler.analyseWithAthanor(input, None)
      val result = Await.result(analysis, 240 seconds)
      assert(result.analytics==Vector(Vector("ContrastAnalysis", "contrast"), Vector()))
      assert(result.message=="ok")
      assert(result.querytime == -1)
    }
  }
}
