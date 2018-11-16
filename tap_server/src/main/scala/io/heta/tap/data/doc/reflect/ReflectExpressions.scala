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

package io.heta.tap.data.doc.reflect

import io.heta.tap.data.doc.Analytics
import io.nlytx.expressions.data.{Coded, Reflect}
import play.api.libs.json.{JsValue, Json, OWrites}

object ReflectExpressions {
  implicit val tsWrites: OWrites[ReflectExpressions] = Json.writes[ReflectExpressions]
  implicit val refWrites: OWrites[Reflect] = Json.writes[Reflect]
  implicit val cWrites: OWrites[Coded] = Json.writes[Coded]
}
case class ReflectExpressions(counts:Reflect, summary:Summary, tags:Seq[Coded]) extends Analytics {
  def asJson: JsValue = Json.toJson(this)
}
