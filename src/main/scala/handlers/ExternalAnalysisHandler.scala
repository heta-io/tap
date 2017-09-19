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

import java.io.File

import akka.stream.ActorMaterializer
import models.QueryResults.StringListResult
import play.api.Logger.logger
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.ws.ahc._
import com.typesafe.config.ConfigFactory
import play.api._
import play.api.libs.ws._
import scala.concurrent.duration.DurationInt
import au.edu.utscic.tap.pipelines.materialize.PipelineContext._
/**
  * Created by andrew@andrewresearch.net on 19/9/17.
  */
object ExternalAnalysisHandler {

  val configuration = Configuration.reference ++ Configuration(ConfigFactory.parseString(
    """
      |ws.followRedirects = true
    """.stripMargin))

  // If running in Play, environment should be injected
  val environment = Environment(new File("."), this.getClass.getClassLoader, Mode.Prod)
  val wsConfig = AhcWSClientConfigFactory.forConfig(configuration.underlying, environment.classLoader)
  val wsClient: WSClient = AhcWSClient(wsConfig)

  def analyseWithAthanor(text:String):Future[StringListResult] = {
    //logger.info(s"Analysing with athanor: $text")
    val url = "http://athanor.utscic.edu.au/v2/analyse/text/rhetorical"
    val request: WSRequest = wsClient.url(url)

    val athanorRequest: WSRequest =
      request.withHttpHeaders("Accept" -> "application/json")
        .withRequestTimeout(10000.millis)

    val futureResponse: Future[WSResponse] = athanorRequest.post(text)

    case class AthanorMsg(message:String, results:List[List[String]])
    import play.api.libs.json._
    import play.api.libs.functional.syntax._
    implicit val AMWrites: Writes[AthanorMsg] = (
      (JsPath \ "message").write[String] and
        (JsPath \ "results").write[List[List[String]]]
    )(unlift(AthanorMsg.unapply))

    implicit val AMReads:Reads[AthanorMsg] = (
      (JsPath \ "message").read[String] and
        (JsPath \ "results").read[List[List[String]]]
      )(AthanorMsg.apply _)

    val result:Future[List[List[String]]] = futureResponse.map { response =>
      (response.json).as[AthanorMsg].results
    }

    //result.foreach(s => logger.warn(s"Response: $s"))


    //logger.warn(s"analyseWithAthanor not implemented. Returning dummy result. Text received:\n $text")
    //val dummyResult:List[List[String]] = List(List("Moves","Not","Implemented"),List("Sentence","Two"))
    //StringListResult(dummyResult)
    result.map(s => StringListResult(s))
  }

}
