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
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers.{BAD_REQUEST, GET, POST, CONTENT_TYPE, NOT_FOUND, OK, contentAsString, contentType, defaultAwaitTimeout, route, status, writeableOf_AnyContentAsEmpty}


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

    "send 400 on a bad request" in  {
      route(app, FakeRequest(GET, s"/health",
        FakeHeaders(Seq.empty),
        play.api.libs.json.Json.obj("email" -> "abc@mail.com", "password" -> "123")
      )).map(status) mustBe Some(BAD_REQUEST)
    }

    "send 200 on a good / request" in  {
      val request = FakeRequest(GET, s"/")
      val response = route(app, request).value
      status(response) mustEqual OK
    }

    "send 200 on a good graphiql request" in  {
      val request = FakeRequest(GET, s"/graphiql")
      val response = route(app, request).value
      status(response) mustEqual OK
    }

    "send 200 on a good health request" in  {
      val request = FakeRequest(GET, s"/health")
      val response = route(app, request).value
      status(response) mustEqual OK
    }

    "send 200 on a good assets request" in  {
      val request = FakeRequest(GET, s"/assets/images/favicon.ico")
      val response = route(app, request).value
      status(response) mustEqual OK
    }

    "send 200 on a good versionedAssets request" in  {
      val request = FakeRequest(GET, s"/versionedAssets/images/favicon.ico")
      val response = route(app, request).value
      status(response) mustEqual OK
    }

    "get 400 on a bad graphiql request" in  {
      val request = FakeRequest(POST, s"/graphql",
        FakeHeaders(Seq.empty),
        play.api.libs.json.Json.obj("input" -> "This is a test."))
      val response = route(app, request).value
      status(response) mustEqual BAD_REQUEST
    }

    /*
    "get 200 on a good graphiql request" in  {
      val exampleQuery = "query MakeVisible($input: String!) {\n  visible(text:$input) {\n    analytics\n  }\n}\n\n"
      val exampleVariable = play.api.libs.json.Json.obj("input" -> "This is a test.")

      val headers = FakeHeaders(Seq("Accept" -> "application/json"))
      val body = play.api.libs.json.Json.obj("query" -> exampleQuery, "variables" -> exampleVariable, "operationName" -> "MakeVisible")
      val request = FakeRequest(POST, s"/graphql",
        headers,
        body,
        "include")
      val response = route(app, request).value
      status(response) mustEqual OK
    }
    */
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

  "GraphQlController" should {

    "render the graphql page" in {
      val home = route(app, FakeRequest(GET, "/graphiql")).get

      status(home) mustBe Status.OK
      contentType(home) mustBe Some("text/html")

      val pageExcerpt1 =
        """<!DOCTYPE html>
          |
          |<html lang="en">
          |  <head>
          |    <meta charset="utf-8">
          |    <meta http-equiv="X-UA-Compatible" content="IE=edge">
          |    <meta name="viewport" content="width=device-width, initial-scale=1">
          |    <meta name="keywords" content="sangria,scala,graphql,playframework,graphiql" />
          |
          |    <link rel="shortcut icon" type="image/png" href="/assets/images/favicon.png">
          |
          |    <title>Explore TAP with GraphiQL</title>
          |
          |    <link href="/assets/css/graphiql.css" rel="stylesheet" />
          |
          |    <script src="/assets/js/react.min.js"></script>
          |    <script src="/assets/js/react-dom.min.js"></script>
          |    <script src="/assets/js/graphiql.min.js"></script>
          |
          |  </head>
          |
          |  <body>
          |    <div id="graphiql">Loading...</div>
          |    <script src="/assets/js/app-graphiql.js"></script>""".stripMargin
      val pageExcerpt2 =
        """  </body>
          |</html>""".stripMargin

      contentAsString(home) must include (pageExcerpt1)
      contentAsString(home) must include (pageExcerpt2)
    }

  }

  "HealthController" should {

    "render the health page" in {
      val home = route(app, FakeRequest(GET, s"/health")).get

      status(home) mustBe Status.OK
      contentType(home) mustBe Some("application/json")

      val pageExcerpt ="{\"message\":\"Ok\"}"

      contentAsString(home) must include (pageExcerpt)
    }

  }

}
