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

import controllers.Assets

/**
  *
  * "Includes", include many UI functions such as bootstrapJS, fontAwesomeJS, reactJS, reactDomJS, graphiqlJS,
  * also provides sources to all the functions in relation to JavaScript.
  *
  */

object Includes {

  import scalatags.Text.all._

  //CSS
//  val bootstrapCSS = link(rel:="stylesheet",
//    href:="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css",
//    attr("integrity"):="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB",
//    attr("crossorigin"):="anonymous"
//  )

  //val graphiqlCSS = link(rel:="stylesheet",href:=Assets.at("stylesheets/graphiql.css"))

//  val graphiqlCSS = link(rel:="stylesheet",
//    href:="https://cdnjs.cloudflare.com/ajax/libs/graphiql/0.11.11/graphiql.css"
//  )

  //Javascript
  val bootstrapJS = script(
    src:="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js",
    attr("integrity"):="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T",
    attr("crossorigin"):="anonymous"
  )

  val fontAwesomeJS = script(
    src:="https://use.fontawesome.com/releases/v5.0.10/js/all.js",
    attr("integrity"):="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+",
    attr("crossorigin"):="anonymous"
  )

  val reactJS = script(
    attr("crossorigin"):="anonymous",
    src:="https://unpkg.com/react@16/umd/react.production.min.js"
  )

  val reactDomJS = script(
    attr("crossorigin"):="anonymous",
    src:="https://unpkg.com/react-dom@16/umd/react-dom.production.min.js"
  )

  val graphiqlJS = script(
    src:="https://cdnjs.cloudflare.com/ajax/libs/graphiql/0.11.11/graphiql.min.js"
  )

}
