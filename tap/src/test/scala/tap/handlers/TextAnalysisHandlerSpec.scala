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

package tap.handlers

import tap.UnitSpec

/**
  * Created by andrew@andrewresearch.net on 20/2/17.
  */


class TextAnalysisHandlerSpec extends UnitSpec {


//  "lineSource" should "provide a list of lines" in {
//    val sourceUnderTest = TextAnalysisHandler.linesSource("This is a test\nanother test\nHere is a longer line")
//
//    val future = sourceUnderTest.runWith(Sink.seq)
//    val result = Await.result(future, 3.seconds)
//    assert(result == Seq("This is a test", "another test", "Here is a longer line"))
//  }
//
//  "replaceSpaces" should "replace space characters with underscores" in {
//    val (pub, sub) = TestSource.probe[String]
//      .via(TextAnalysisHandler.replaceSpaces)
//      .toMat(TestSink.probe[String])(Keep.both)
//      .run()
//
//    sub.request(n = 3)
//    pub.sendNext("This is a test")
//    pub.sendNext("another test")
//    pub.sendNext("Here is a longer line")
//    sub.expectNextUnordered("This_is_a_test", "another_test", "Here_is_a_longer_line")
//  }
//  /*
//  pub.sendError(new Exception("Power surge in the linear subroutine C-47!"))
//  val ex = sub.expectError()
//  assert(ex.getMessage.contains("C-47"))
//*/
//  "outputFormat" should "create a single string from separate lines" in {
//    val future = Source(List("This_is_a_test", "another_test", "Here_is_a_longer_line")).runWith(TextAnalysisHandler.outputFormat)
//    val result = Await.result(future, 3.seconds)
//    assert(result == "start|This_is_a_test|another_test|Here_is_a_longer_line")
//  }
}
