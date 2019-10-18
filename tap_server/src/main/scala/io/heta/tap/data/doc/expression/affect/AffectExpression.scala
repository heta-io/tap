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

package io.heta.tap.data.doc.expression.affect

import io.heta.tap.data.doc.Analytics
import io.heta.tap.data.doc.expression.Expression
import play.api.libs.json.{JsValue, Json, OWrites}


/** Data object of AffectExpression that gets returned to the browser */
object AffectExpression {
  implicit val ttWrites: OWrites[AffectExpression] = Json.writes[AffectExpression]
}
case class AffectExpression(text: String, startIdx: Int, endIdx: Int,
                            valence: Double = 0.0, arousal: Double = 0.0, dominance: Double = 0.0) extends Expression with Analytics {
  override def asJson: JsValue = Json.toJson(this)
}
