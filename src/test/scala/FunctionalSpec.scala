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


import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Play
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers.{BAD_REQUEST, GET, NOT_FOUND, OK, contentAsString, contentType, defaultAwaitTimeout, route, status, writeableOf_AnyContentAsEmpty}


/**
  * Created by andrew@andrewresearch.net on 25/8/17.
  */

class FunctionalSpec extends PlaySpec with GuiceOneAppPerSuite {

  /* Application start */
  "start the Application" in {
    Play.maybeApplication mustBe Some(app)
  }

  /* Check all routes */
  "Routes" should {

    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/boum")).map(status) mustBe Some(NOT_FOUND)
    }

    "send 200 on a good request" in  {
      val request = FakeRequest(GET, s"/")
      val response = route(app, request).value
      status(response) mustEqual OK
    }

  }

  /* Check controllers */
  "DefaultController" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe Status.OK
      contentType(home) mustBe Some("text/html")

      val pageExcerpt =
        """<!DOCTYPE html>
          |<html lang="en">
          |<head>
          |  <meta charset="UTF-8">
          |  <title>TAP Server</title>
          |</head>
          |<body>
          |<h1>TAP Server with GraphQL</h1>
          |  <a href="/graphiql">Use the graphiql IDE here</a>
          |</body>
          |</html>""".stripMargin

      contentAsString(home) must include (pageExcerpt)
    }

  }

}
