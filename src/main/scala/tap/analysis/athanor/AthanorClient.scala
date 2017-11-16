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

package tap.analysis.athanor

import javax.inject.Inject

import tap.util.AppConfig
import models.Results.StringListResult
import play.api.Logger
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._ // scalastyle:ignore


/**
  * Created by andrew@andrewresearch.net on 6/11/17.
  */

class AthanorClient @Inject()(wsClient: WSClient, config: AppConfig)(implicit ec: ExecutionContext) {

  val logger: Logger = Logger(this.getClass)

  val athanorURL= config.getAthanorURL()

  def process(text:String,parameter:String):Future[StringListResult] = {
    //logger.info(s"Analysing with athanor: $text")

    val url = athanorURL + parameter
    logger.info(s"Analysing with athanor at this url: $url")

    val request: WSRequest = wsClient.url(url)

    val athanorRequest: WSRequest = request
      .withHttpHeaders("Accept" -> "application/json")
      .withRequestTimeout(30000.millis)

    val futureResponse: Future[WSResponse] = athanorRequest.post(text)


    val result: Future[StringListResult] = {
      val decoded = futureResponse.map { response =>
        val res = decodeRepsonse(response)
        StringListResult(res,"ok")
      }
      val errMsg = "There was a problem connecting to the Athanor server."
      futureResponse.recover {
        case e: Any => {
          val msg = s"$errMsg: $e"
          logger.error(msg)
          StringListResult(Vector(),msg)
        }
        //case _ => logger.error(errMsg)
      }
      decoded
    }
    result
  }

  case class AthanorMsg(message: String, results: Vector[Vector[String]])

  def decodeRepsonse(response:WSResponse): Vector[Vector[String]] = {
    val resBody = response.body
    if(resBody.nonEmpty && resBody.contains(":[[")) {
      logger.debug("Decoding response: "+ resBody)

      import play.api.libs.functional.syntax._ //scalastyle:ignore
      import play.api.libs.json._ //scalastyle:ignore

      implicit val AMWrites: Writes[AthanorMsg] = (
        (JsPath \ "message").write[String] and
          (JsPath \ "results").write[Vector[Vector[String]]]
        ) (unlift(AthanorMsg.unapply))

      implicit val AMReads: Reads[AthanorMsg] = (
        (JsPath \ "message").read[String] and
          (JsPath \ "results").read[Vector[Vector[String]]]
        ) (AthanorMsg.apply _)

      val athanorMsg:AthanorMsg = response.json.as[AthanorMsg]
      logger.debug("Athanor message: " + athanorMsg.message)
      logger.debug("Athanor results: " + athanorMsg.results)
      athanorMsg.results
    } else {
      logger.error("There was a problem: " + resBody) //TODO If we get to here, we need to return the message to the client!!
      Vector()
    }
  }
}
