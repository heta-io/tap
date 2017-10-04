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

package controllers

import javax.inject.Inject

import handlers.GraphQlHandler
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.{Action, AnyContent, InjectedController}

/**
  * Created by andrew@andrewresearch.net on 22/8/17.
  */

class GraphQlController @Inject() (assets: AssetsFinder) extends InjectedController {

  def graphiql:Action[AnyContent] = Action {
    Ok(views.html.graphiql(assets))
  }

  def graphql:Action[JsValue] = Action.async(parse.json) { request =>
    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]
    val variables = (request.body \ "variables").asOpt[JsObject].getOrElse(Json.obj())
    GraphQlHandler.process(query,operation,variables)
  }

}
