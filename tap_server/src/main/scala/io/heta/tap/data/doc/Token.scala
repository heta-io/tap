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
  * Created by andrew@andrewresearch.net on 29/8/17.
  */

/**
  * JSON Web Token (JWT) allows tokens to be easily transmitted via query strings,
  * header attributes and within the body of a POST request.
  *
  * Data object of Token that gets returned to the browser
  */

object Token {
  implicit val ttWrites: OWrites[Token] = Json.writes[Token]
}
case class Token(idx:Int, term:String, lemma:String, postag:String, nertag:String,
                 parent:Int, children:Vector[Int], deptype:String, isPunctuation:Boolean) {
  def asJson: JsValue = Json.toJson(this)
}
