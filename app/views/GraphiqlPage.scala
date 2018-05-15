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
import scalatags.Text.tags2
import scalatags.Text.tags
import scalatags.Text.TypedTag

object GraphiqlPage extends GenericPage {

  override def page(titleStr:String):TypedTag[String] = tags.html(
    head(
      tags2.title(titleStr),
      link(rel:="stylesheet",href:=routes.Assets.versioned("stylesheets/bootstrap.min.css").url),
      link(rel:="stylesheet",href:=routes.Assets.versioned("stylesheets/graphiql.css").url)
    ),
    body(
      div(id:="graphiql")(),
      script(src:=routes.Assets.versioned("clientjs-fastopt-bundle.js").url)
    )
  )

}
