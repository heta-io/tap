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

package io.heta.tap.data.doc.expression.reflect

import play.api.libs.json.{Json, OWrites}

/** Data object of Summary that gets returned to the browser */
object Summary {
  implicit val tsWrites: OWrites[Summary] = Json.writes[Summary]
}
case class Summary(metaTagSummary:MetaTagSummary,
                   phraseTagSummary:PhraseTagSummary)
