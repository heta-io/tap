// Copyright (C) 2017 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package handlers

import models.QuerySchema
import play.api.libs.json.JsObject
import play.api.mvc.Result
import play.api.mvc.Results.{BadRequest, InternalServerError, Ok}
import sangria.ast.Document
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.playJson.{PlayJsonInputUnmarshallerJObject, PlayJsonResultMarshaller}
import sangria.parser.{QueryParser, SyntaxError}
import sangria.schema.Schema

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by andrew@andrewresearch.net on 25/8/17.
  */

object GraphQlHandler {

  lazy val schema:Schema[QueryActions,Unit] = QuerySchema.createSchema
  lazy val actions:QueryActions = new QueryActions

  def process(query:String,name:Option[String],variables:JsObject):Future[Result] = QueryParser.parse(query) match {
    case Success(queryAst) => executeGraphQLQuery(queryAst, name, variables)
    case Failure(error: SyntaxError) => Future.successful(BadRequest(error.getMessage))
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
