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

import play.api.libs.json.{JsDefined, JsObject, JsString, Json}

import scala.util.Try

val as = List.range(1,10)
val bs = List.range(1,10)
val cs = List.range(1,10)

for {
  ((a,b),c) <- as zip bs zip cs
} yield (a,b,c)

val parameters = Option("""{
    |"cleanType": "visible"
    |}""".stripMargin)

val jsonResult = Try(Json.parse(parameters.getOrElse("{}")))

val jparam = jsonResult.getOrElse(JsObject(Seq()))

  val result = (jparam \ "cleanType").getOrElse(JsString("")) match {
    case JsString("visible") => "VIS"
    case JsString("ascii") => "ASCII"
    case _ => "No Value"
  }

println(s"RESULT: $result")



