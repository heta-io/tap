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

package io.heta.tap.data.results

import io.heta.tap.data.doc.{Metrics, Sentence}
import play.api.libs.json.{JsValue, Json, OWrites}

object MetricsResult {
  implicit val mWrites: OWrites[MetricsResult] = Json.writes[MetricsResult]
}
case class MetricsResult(analytics:Metrics, message: String = "", querytime: Int = -1, name: String="") extends Result with Batch {
  def asJson: JsValue = Json.toJson(this)
}

