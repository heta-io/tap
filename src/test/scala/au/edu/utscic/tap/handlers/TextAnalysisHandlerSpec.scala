package au.edu.utscic.tap.handlers

import au.edu.utscic.tap.UnitSpec

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
