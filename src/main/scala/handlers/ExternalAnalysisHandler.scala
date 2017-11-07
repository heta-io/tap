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

package handlers

import javax.inject.Inject

import models.Results.StringListResult
import play.api.Logger
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import tap.pipelines.materialize.PipelineContext.executor


import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
/**
  * Created by andrew@andrewresearch.net on 19/9/17.
  */
class ExternalAnalysisHandler @Inject() (wsClient: WSClient) {

  val logger: Logger = Logger(this.getClass)

  def analyseWithAthanor(text:String,grammar:Option[String]):Future[StringListResult] = {
    //logger.info(s"Analysing with athanor: $text")
    val parameter = "?grammar=" + grammar.getOrElse("analytic")
    val url = "http://athanor.utscic.edu.au/v2/analyse/text/rhetorical" + parameter
    logger.info(s"Creating request to: $url")
    val request: WSRequest = wsClient.url(url)

    val athanorRequest: WSRequest = request
      .withHttpHeaders("Accept" -> "application/json")
      .withRequestTimeout(30000.millis)

    val futureResponse: Future[WSResponse] = athanorRequest.post(text)
    //try {
    case class AthanorMsg(message: String, results: Vector[Vector[String]])

    import play.api.libs.functional.syntax._
    import play.api.libs.json._ //scalastyle:ignore

    implicit val AMWrites: Writes[AthanorMsg] = (
      (JsPath \ "message").write[String] and
        (JsPath \ "results").write[Vector[Vector[String]]]
      ) (unlift(AthanorMsg.unapply))

    implicit val AMReads: Reads[AthanorMsg] = (
      (JsPath \ "message").read[String] and
        (JsPath \ "results").read[Vector[Vector[String]]]
      ) (AthanorMsg.apply _)
    logger.warn("About to try and get result...")
    val result: Future[StringListResult] = {
      futureResponse.map { response =>
        val res = response.json.as[AthanorMsg].results
        StringListResult(res,"ok")
      }
      val errMsg = "There was a problem connecting to the Athanor server."
      futureResponse.recover {
        case e: Any => {
          val msg = s"$errMsg: $e"
          logger.error(msg)
          StringListResult(Vector(),msg)
        }
      }.asInstanceOf[Future[StringListResult]]
    }
    result
  }

}
