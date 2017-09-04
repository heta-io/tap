// Copyright (C) 2017 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package models

import java.time.OffsetDateTime

/**
  * Created by andrew@andrewresearch.net on 29/8/17.
  */

object QueryResults {

  trait Result {
    val timestamp: String = OffsetDateTime.now().toString
  }

  case class StringResult(text: String) extends Result

  case class TokensResult(count: Int, lemmas: List[String], postags: List[String]) extends Result

  trait AnalyticsResult {
    val result: Result
    val message: Option[String] = None
    val link: Option[String] = None
  }

  case class StringAnalyticsResult(result:StringResult,message: Option[String] = None, link: Option[String] = None)

  case class TokensAnalyticsResult(result:TokensResult,message: Option[String] = None, link: Option[String] = None)





}