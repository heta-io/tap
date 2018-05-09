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

package models
import play.api.libs.json._

case class HealthStatus(code: Int)

object HealthStatus {

  implicit object HealthStatusFormat extends Format[HealthStatus] {
    // convert from JSON string to a HealthStatus object (de-serializing from JSON)
    def reads(json: JsValue): JsResult[HealthStatus] = {
      val code = (json \ "code").as[Int]
      JsSuccess(HealthStatus(code))
    }

    // convert from HealthStatus object to JSON (serializing to JSON)
    def writes(s: HealthStatus): JsValue = {
      val code = JsNumber(s.code).toString()
      code match{
        case "200" => JsObject(Seq("message" -> JsString("Ok")))
        case whoa  => JsObject(Seq("message" -> JsString("Unexpected code: " + whoa)))
      }
    }
  }
}
