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
package tap

import org.scalatestplus.play.PlaySpec
import play.api.inject.guice.GuiceApplicationBuilder
import tap.analysis.wordvector.WordVector

import scala.concurrent.Await
import scala.concurrent.duration._

class WordVectorSpec extends PlaySpec{

  //dependency injection
  private val app = new GuiceApplicationBuilder().build
  private val wordvector = app.injector.instanceOf[WordVector]

  "find neareast words" in  {
    val lst = wordvector.nearestWords("day", 10)
    val result = Await.result(lst, 360 seconds)

    if(result!= None){
      assert(result.get(0) == "week")
      assert(result.get(1) == "days")
      assert(result.get(2) == "morning")
      assert(result.get(3) == "month")
      assert(result.get(4) == "hours")
      assert(result.get(5) == "afternoon")
      assert(result.get(6) == "hour")
      assert(result.get(7) == "weekend")
      assert(result.get(8) == "evening")
      assert(result.get(9) == "time")
    }
  }

}
