
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

/**
  * Created by andrew@andrewresearch.net on 7/11/17.
  */

import sbt._
import Keys._

object LocalSbtSettings {
  //For Documentation links
  val githubBaseUrl = "https://github.com/heta-io/tap"
  val scaladocApiBaseUrl = "https://heta.github.io/tap"
  //For AWS docker deployment
  val dockerRepoURI = "523990814326.dkr.ecr.ap-southeast-2.amazonaws.com/infosci"
}
