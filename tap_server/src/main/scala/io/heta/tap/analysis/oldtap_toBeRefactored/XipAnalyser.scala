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

//package tap.services.analytics.analysers



  /*

  private def analyseWithXip(inputData:InputData):Future[DocumentXip] = {
    for {
      xipJson <- ask(xipClient,inputData).mapTo[String]
      decodedJson <- Future(xmlDecode(xipJson))
      xip <- Future(xipResponsesFromJson(decodedJson))
    } yield xip.getOrElse(DocumentXip(List()))
  }

  private def xipResponsesFromJson(xipJson:String):Option[DocumentXip] = {
    xipFromJson(xipJson) match {
      case Success(data) => Some(DocumentXip(data))
      case Failure(ex) => {
        log.error("Unable to retrieve XIPResponse from json string: "+xipJson.take(100))
        log.debug("Error message: "+ex)
        None
      }
    }
  }

  private def xmlDecode(s:String):String = {
    val decoded = s.replaceAllLiterally("&lt;","<")
      .replaceAllLiterally("&apos;","'")
      .replaceAllLiterally("&gt;",">")
      .replaceAllLiterally("&amp;","&")
    log.debug("decoded text: "+decoded)
    decoded
  }

  implicit val serialformats = Serialization.formats(NoTypeHints)

  private def xipFromJson(jsonStr:String):Try[List[SentenceXip]] = Try {read[List[SentenceXip]](jsonStr)}

*/

