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
object HomePage extends GenericPage {

  override def page(titleStr:String):TypedTag[String] = tags.html(
    head(
      tags2.title(titleStr),
      link(rel:="stylesheet",href:=routes.Assets.versioned("stylesheets/bootstrap.min.css").url)
    ),
    body(
      div(`class`:="container-fluid",
        div(`class`:="row"),
        div(`class`:="row",
          div(`class`:="col-sm-3"),
          div(`class`:="col-sm-6",
            div(`class`:="card card-default",
              div(`class`:="card-header", b(titleStr)),
              div(`class`:="card-body",
                a(href:=routes.GraphQlController.graphiql().url)("Use the graphiql interface to connect to TAP.")
              )
            )
          )
        )
      ),
      script(src:=bundleUrl)
    )
  )

}