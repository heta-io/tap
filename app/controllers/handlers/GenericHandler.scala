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

package controllers.handlers


import io.heta.tap.data.results.StringResult
import play.api.Logger
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.reflect.runtime.universe._
import scala.util.Try

trait GenericHandler {

  def queryTime(start:Long):Int = (System.currentTimeMillis() - start).toInt

  def validJson(parameters:Option[String]):Option[JsValue] = parameters.flatMap(p => Try(Json.parse(p)).toOption).map(_.result.get)

  def extractParameter[A:TypeTag](paramName:String,parameters:Option[String]):Option[Any] = {
    val jsParams = validJson(parameters)
    Logger.debug(s"JSON: $jsParams")
    jsParams.flatMap { jp =>
      val result = Try((jp \ paramName).toOption).toOption.flatten
      typeOf[A] match {
        case t if t =:= typeOf[String] => Try(result.map(_.as[String])).toOption.flatten // scalastyle:ignore
        case t if t =:= typeOf[Double] => Try(result.map(_.as[Double])).toOption.flatten // scalastyle:ignore
        case t if t =:= typeOf[Int] => Try(result.map(_.as[Int])).toOption.flatten       // scalastyle:ignore
        case _ => None
      }
    }
  }

  def dummyResult(text:String):Future[StringResult] = Future {
    StringResult("This features is not implemented yet")
  }


}
