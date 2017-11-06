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

import models.{GraphqlActions, GraphqlSchema}
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.{Action, AnyContent, InjectedController, Result}
import sangria.ast.Document
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.playJson.{PlayJsonInputUnmarshallerJObject, PlayJsonResultMarshaller}
import sangria.parser.{QueryParser, SyntaxError}
import sangria.schema.Schema

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Created by andrew@andrewresearch.net on 22/8/17.
  */

class GraphQlController @Inject() (assets: AssetsFinder, gqlSchema: GraphqlSchema, actions: GraphqlActions) extends InjectedController {

  val schema:Schema[GraphqlActions,Unit] = gqlSchema.create

  def graphiql:Action[AnyContent] = Action {
    Ok(views.html.graphiql(assets))
  }

  def graphql:Action[JsValue] = Action.async(parse.json) { request =>
    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]
    val variables = (request.body \ "variables").asOpt[JsObject].getOrElse(Json.obj())
    process(query,operation,variables)
  }

  //lazy val schema:Schema[GraphqlSchema.Actions,Unit] = GraphqlSchema.create
  //lazy val actions:GraphqlSchema.Actions = GraphqlSchema.actions

  def process(query:String,name:Option[String],variables:JsObject):Future[Result] = QueryParser.parse(query) match {
    case Success(queryAst) => executeGraphQLQuery(queryAst, name, variables)
    case Failure(error: SyntaxError) => Future.successful(BadRequest(error.getMessage))
    case _ => Future.successful(BadRequest("There was a problem with the request to TAP graphql."))
  }

  def executeGraphQLQuery(query: Document, name: Option[String], vars: JsObject):Future[Result] = {

    Executor.execute(schema, query, actions, operationName = name, variables = vars)
      .map(Ok(_))
      .recover {
        case error: QueryAnalysisError => BadRequest(error.resolveError)
        case error: ErrorWithResolver => InternalServerError(error.resolveError)
      }

  }
}





