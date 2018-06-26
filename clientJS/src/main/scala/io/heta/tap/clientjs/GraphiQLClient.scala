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

package io.heta.tap.clientjs


import org.scalajs.dom
import slinky.web.ReactDOM
import slinky.web.html.{a, h1, h3}

object GraphiQLClient {

  def main(args: Array[String]): Unit = {
//    println("This is the GraphiQLClient 4")
//    val text:String = "From .js: "+graphiqlsetup.myFunc("text passed to graphiql")
//    val test = dom.document.getElementById("test")
//    ReactDOM.render(
//      h3(text),
//      test
//    )

    val graphiqlDiv = dom.document.getElementById("graphiql")

   // println(text)

    // println("loading...")
    val result  = GraphiqlSetup.load(GraphiQLQueries.allQueries, GraphiQLQueries.aeDemoVariables)
    println("result")

  }

}




