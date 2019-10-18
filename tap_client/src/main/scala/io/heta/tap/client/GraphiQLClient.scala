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

package io.heta.tap.client

import org.scalajs.dom
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/** GraphiQL Client */
@JSExportTopLevel("io.heta.tap.client.GraphiQLClient")
object GraphiQLClient {

  @JSExport
  def run(): Unit = {
    println("Loading GraphiQLClient...")

    val graphiqlDiv = dom.document.getElementById("graphiql")
    val result  = GraphiqlSetup.load(GraphiQLQueries.allQueries, GraphiQLQueries.allParams)
    println(s"Load result: $result")
  }

}




