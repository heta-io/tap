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

package models.graphql

object FieldDocs {

  case class FieldDoc(
    description:String,
    parameters:Map[String,String],
    exampleQuery:String
  )

  val fields: Map[String, FieldDoc] = Map{
    "clean" -> FieldDoc(
      """
        |Clean is a query that will clean and format the text depending on which parameters you pass.
        |There are 5 current parameters you can pass.
        |
        |- visible = Replaces all white spaces with dots and new lines with line feeds.
        |- minimal = Removes all extra white spaces and extra new lines, leaving only one of each.
        |- simple = Removes all extra white spaces and extra new lines, leaving only one of each. It will also replace hypens and quotes with their ascii safe equivalents.
        |- preserve = This will replace spaces with dots and preserve the length of the text.
        |- ascii = This will replace all non ascii characters eg any char above 127
        |
        |See [github](https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Clean.ipynb) for examples and descriptions.
      """.stripMargin,
      Map("none"->"This query doesn't take parameters"),
      """
        |query Clean($input: String,$parameters:String) {
        |        clean(text:$input,parameters:$parameters) {
        |          analytics
        |          querytime
        |          message
        |          timestamp
        |        }
        |      }
      """.stripMargin)
  }



}
