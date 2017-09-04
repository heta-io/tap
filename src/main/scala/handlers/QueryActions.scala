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

package handlers

import au.edu.utscic.tap.nlp.factorie.Annotation
import handlers.TextAnalysisHandler.{CLEAN, CLEAN_ASCII, CLEAN_MINIMAL, CLEAN_PRESERVE, VISIBLE, analyse}
import models.QueryResults.{StringAnalyticsResult, TokensAnalyticsResult}

import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 31/8/17.
  */
class QueryActions {

  def visible(text:String):Future[StringAnalyticsResult] = analyse(text,VISIBLE)
  def clean(text:String):Future[StringAnalyticsResult] = analyse(text,CLEAN)
  def cleanPreserve(text:String):Future[StringAnalyticsResult] = analyse(text,CLEAN_PRESERVE)
  def cleanMinimal(text:String):Future[StringAnalyticsResult] = analyse(text,CLEAN_MINIMAL)
  def cleanAscii(text:String):Future[StringAnalyticsResult] = analyse(text,CLEAN_ASCII)
  def tokens(text:String):Future[TokensAnalyticsResult] = Annotation.tokenise(text)
}
