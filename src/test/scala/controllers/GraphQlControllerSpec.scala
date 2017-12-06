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

/**
  * Created by jh on 5 Dec 2017.
  */
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Play
import play.api.http.Status
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers.{BAD_REQUEST, GET, POST, CONTENT_TYPE, NOT_FOUND, OK, contentAsString, contentType, defaultAwaitTimeout, route, status, writeableOf_AnyContentAsEmpty}

import org.scalatestplus.play._
import javax.inject.Inject
import scala.concurrent.Future
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import play.api.Application

import java.io.File
import java.nio.file.Files


import play.api.libs.ws.WSClient
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import akka.stream.scaladsl._
import akka.util.ByteString
import akka.stream.scaladsl.FileIO
import akka.stream.scaladsl.Source
import scala.util.matching.Regex

// class GraphQlControllerSpec @Inject() (ws: WSClient) extends PlaySpec with Results with GuiceOneServerPerSuite  {
class GraphQlControllerSpec extends PlaySpec with Results with Injecting with GuiceOneServerPerSuite {
  "UploadService" should {


    "uploadFile returns ok" in {
      val tmpFile = java.io.File.createTempFile("prefix", "txt")
      tmpFile.deleteOnExit()
      val msg = "hello world"
      Files.write(tmpFile.toPath, msg.getBytes())

      val url = s"http://localhost:${play.api.test.Helpers.testServerPort}/upload"
      val request = inject[WSClient].url(url)
      val responseFuture = request.post(postSource(tmpFile))
      val response = await(responseFuture)
      response.status mustBe OK

      // The id returned has the format randomNumber-extCode-fileSize
      // where randomNumber is part of the uploaded file name and can
      // be later used to relocate it. The fileSize is not part of the
      // uploaded file name but can be used to verify its contents.
      // extCode is "01" for text files, "02" for zip, "03" for jar and "00" for other files
      val id_pattern = new Regex("[0-9]+" + "-01-" + "[0-9]+")
      val id = id_pattern.findFirstIn(response.body)

      val matching = id match {
        case Some(x) => true
        case None => false
      }

      matching mustBe true
    }


    //
    // This testcase causes an exception in the server custom upload handler that I could not
    // figure out how to catch since the handler is invoked automatically. I tried the
    // simple upload examples in the Play framework documentation, but the exception still
    // happened, and I could not catch it as it is seems to be happening inside the play
    // framework handlers and does not seem to be propagated up to the server code.
    // The tescase deletes the file to be uploaded after preparing the POST request,
    // and I tried different upload code that is available online, but none seems to
    // be able to handle it. It could be that the tescase is not valid, or not typical
    // but I left it in comments in case I learn more how to handle it later.
    //
    //
    //    "uploadFile returns fail" in {
    //      val tmpFile = java.io.File.createTempFile("prefix", "txt")
    //      val msg = "hello world"
    //      Files.write(tmpFile.toPath, msg.getBytes())

    //      val url = s"http://localhost:${play.api.test.Helpers.testServerPort}/upload"
    //      val request = inject[WSClient].url(url)

    //
    // Delete the file so the upload request is not valid
    //
    //      tmpFile.delete()
    //      val responseFuture = request.post(postSource(tmpFile))
    //      val response = await(responseFuture)
    //      response.status mustBe ???
    //    }
  }

  def postSource(tmpFile: File): Source[MultipartFormData.Part[Source[ByteString, _]], _] = {
    import play.api.mvc.MultipartFormData.FilePart
    import play.api.mvc.MultipartFormData.DataPart


    val akkaSource = akka.stream.scaladsl.FileIO.fromPath(tmpFile.toPath)

    val filePart = play.api.mvc.MultipartFormData.FilePart(
      "name",
      "hello.txt",
      Option("text/plain"),
      akkaSource
    )

    val dataPart = play.api.mvc.MultipartFormData.DataPart("key", "value")

    akka.stream.scaladsl.Source(filePart :: dataPart :: List())

  }
}