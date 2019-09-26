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

import play.twirl.api.Html
import scalatags.Text
import scalatags.Text.all._ // scalastyle:ignore
import scalatags.Text.{tags, tags2}

/**
  * Provides render and page functions for other HTML pages.
  */

trait GenericPage {

  /** Renders HTML page data */
  def render(title:String):Html = Html("<!DOCTYPE html>" + page(title).render)
  /** Renders HTML page data */
  def page(titleStr:String):Text.TypedTag[String] = tags.html(head(tags2.title(titleStr)))
  /** Bundles URL */
  def bundleUrl: String = Seq("client-opt-bundle.js", "client-fastopt-bundle.js")
      .find(name => getClass.getResource(s"/public/$name") != null)
      .map(name => controllers.routes.Assets.versioned(s"$name").url).getOrElse("BUNDLE_NOT_FOUND")
}
