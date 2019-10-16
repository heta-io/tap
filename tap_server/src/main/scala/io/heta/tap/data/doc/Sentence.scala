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

package io.heta.tap.data.doc

import play.api.libs.json.{JsValue, Json, OWrites}

/**
  * Created by andrew@andrewresearch.net on 6/9/17.
  */

object Sentence {
  implicit val tsWrites: OWrites[Sentence] = Json.writes[Sentence]
}
case class Sentence(original:String, tokens: Vector[Token], start: Int, end: Int, length: Int, idx: Int) {
  def asJson: JsValue = Json.toJson(this)
}
