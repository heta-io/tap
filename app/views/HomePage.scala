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

package views

import controllers.routes
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags, tags2}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */

/**
  * HomePage shows the introductory page of TAP which contains TAP docs, source code,
  * example queries and the graphiQL interface for TAP.
  */
object HomePage extends GenericPage {

  override def page(titleStr:String):TypedTag[String] = tags.html(
    head(
      tags2.title(titleStr),
      link(rel:="stylesheet",href:=routes.Assets.versioned("stylesheets/bootstrap.min.css").url)
    ),
    body(
      div(`class`:="container-fluid",
        div(`class`:="row",
          div(`class`:="col",
            h3("Text Analytics Pipeline (TAP)")
          )
        ),
        div(`class`:="row",
          div(`class`:="col-1"),
          div(`class`:="col-5",
            div(`class`:="card card-default",
              div(`class`:="card-header", b("Learn more")),
              div(`class`:="card-body",
                p(
                  "Read the ",a(href:="https://heta-io.github.io/tap/",target:="docs")("docs")
                ),
                p(
                  "Get the ",a(href:="https://github.com/heta-io/tap",target:="source")("source code")
                ),
                p(
                  "Get ",a(href:="/queries",target:="queries")("example queries")
                )
              )
            )
          ),
          div(`class`:="col-5",
            div(`class`:="card card-default",
              div(`class`:="card-header", b("Try TAP")),
              div(`class`:="card-body",
                p(
                  "Use the ",a(href:=routes.GraphQlController.graphiql().url)("graphiql interface")," to connect to TAP."
                )
              )
            )
          )
        )
      ),
      script(src:=bundleUrl)
    )
  )

}