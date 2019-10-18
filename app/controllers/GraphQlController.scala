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

package controllers

import javax.inject.Inject
import models.GraphqlSchema
import models.graphql.GraphqlActions
import play.api.Logger
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.{Action, AnyContent, InjectedController, Result}
import sangria.ast.Document
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.playJson.{PlayJsonInputUnmarshallerJObject, PlayJsonResultMarshaller}
import sangria.parser.{QueryParser, SyntaxError}
import sangria.schema.Schema
import views.GraphiqlPage

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
/**
  * Created by andrew@andrewresearch.net on 22/8/17.
  */

/**
  * Handles all GraphQL requests.
  *
  * @param assets used to find assets according to configured base path and URL base.
  * @param gqlSchema describes the tables and corresponding fields contained in the Graphql.
  * @param actions handles requests.
  */

class GraphQlController @Inject() (assets: AssetsFinder, gqlSchema: GraphqlSchema, actions: GraphqlActions) extends InjectedController {

  val schema:Schema[GraphqlActions,Unit] = gqlSchema.create

  /**
    * An action function that handles request in any content type and generates a result to be sent to the client.
    * (Request[A] => Result)
    *
    * @return A [[play.api.mvc.Action]] containing [[AnyContent]] type and generates a result to be sent to the client.
    */
  def graphiql:Action[AnyContent] = Action {
    request => Logger.info("Got Any content request from:" + request.remoteAddress)
    //Ok(views.html.graphiql(assets))
    Ok(GraphiqlPage.render("Explore TAP with GraphiQL"))
  }

  /**
    * An action function that handles request in generic json value type and generates a result to be sent to the client.
    * (Request[A] => Result)
    *
    * @return A [[play.api.mvc.Action]] containing [[JsValue]] type and generates a result to be sent to the client.
    */
  def graphql:Action[JsValue] = Action.async(parse.json) { request =>
    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]
    val variables = (request.body \ "variables").asOpt[JsObject].getOrElse(Json.obj())
    Logger.info(s"Query received from ${request.remoteAddress} >>> ${operation.getOrElse("No query")}")
    Logger.info(s"Variables: $variables")
    process(query,operation,variables)
  }

  /**
    * Check if the parsed query has succeed or failed to process else produce the message of "There was a problem with the request to TAP graphql.".
    *
    * @param query Documents to return
    * @param name Optional version name of the object
    * @param variables Json objects
    * @return Success or Failure else produce the message of "There was a problem with the request to TAP graphql.".
    */
  def process(query:String,name:Option[String],variables:JsObject):Future[Result] = QueryParser.parse(query) match {
    case Success(queryAst) => executeGraphQLQuery(queryAst, name, variables)
    case Failure(error: SyntaxError) => Future.successful(BadRequest(error.getMessage))
    case _ => Future.successful(BadRequest("There was a problem with the request to TAP graphql."))
  }

  /**
    * Execution of GraphQLQuery
    *
    * @param query Documents to return
    * @param name Optional version name of the object
    * @param vars Json objects
    * @return A [[scala.concurrent.Future Future]] of [[Result]] type
    */
  def executeGraphQLQuery(query: Document, name: Option[String], vars: JsObject):Future[Result] = {
     Executor.execute(schema, query, actions, operationName = name, variables = vars)
      .map(Ok(_))
      .recover {
        case error: QueryAnalysisError => BadRequest(error.resolveError)
        case error: ErrorWithResolver => InternalServerError(error.resolveError)
      }

  }
}





