/*
 * Copyright 2016-2017 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//
//
//import org.scalatestplus.play._
//import org.scalatestplus.play.guice.{GuiceOneAppPerSuite, GuiceOneServerPerTest}
//
///**
//  * Created by andrew@andrewresearch.net on 25/8/17.
//  */
//
///**
//  * Runs a browser test using Fluentium against a play application on a server port.
//  */
//class BrowserSpec extends PlaySpec with GuiceOneAppPerSuite
//  //with OneBrowserPerTest
//  //with GuiceOneServerPerTest
//  //with HtmlUnitFactory
//  //with ServerProvider
//{
//
//  "Application" should {
//
//    "work from within a browser" in {
//
//      go to ("http://localhost:" + port)
//
//      pageSource must include ("Testing GraphQL")
//    }
//  }
//}