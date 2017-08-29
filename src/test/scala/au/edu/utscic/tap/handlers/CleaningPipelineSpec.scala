package au.edu.utscic.tap.handlers

import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.util.ByteString
import au.edu.utscic.tap.{TapStreamContext, UnitSpec}
import au.edu.utscic.tap.pipelines.Cleaning
import au.edu.utscic.tap.pipelines.Cleaning._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by andrew@andrewresearch.net on 4/7/17.
  */

class CleaningPipelineSpec extends UnitSpec {

  import TapStreamContext._

  def testSource(input:String) = Source.single(ByteString(input))
  val testSink = Flow[String].toMat(Sink.head[String])(Keep.right)

  "revealInvisible" should "replace whitespace characters with visible characters" in {

    import White._
    val input = s"1${sp}2${nb}3${nl}4${cr}5\u001e6\u00807"
    val future = testSource(input) via Cleaning.Pipeline.revealInvisible runWith testSink
    val result = Await.result(future, 3 seconds)
    assert(result=="1·2·3¬4¬5�6�7")
  }

  "simplify" should "replace quotes and hyphens with single byte versions" in {

    import Quote._
    val input = s"1${singleCurlyLeft}2${singleCurlyRight}3${doubleCurlyLeft}4${doubleCurlyRight}5${Hyphen.rgx_hyphens}6"
    val future = testSource(input) via Cleaning.Pipeline.simplify runWith testSink
    val result = Await.result(future, 3 seconds)
    assert(result=="1'2'3\"4\"5-|-|-|-|-|-|-|-6")
  }

  "lengthPreserve" should "replace control characters while preserving length" in {
    import White._
    val input = s"1${sp}2${nb}3${nl}4${cr}5\u001e6\u00807"
    val future = testSource(input) via Cleaning.Pipeline.lengthPreserve runWith testSink
    val result = Await.result(future, 3 seconds)
    assert(result=="1 2 3\n4\n5�6�7" && result.length==input.length)
  }

  "utfMinimal" should "strip control characters, and reduce whitespace" in {
    import White._
    val input = s"1${sp}${nb}3${nl}${cr}5\u001e6\u00807"
    val future = testSource(input) via Cleaning.Pipeline.utfMinimal runWith testSink
    val result = Await.result(future, 3 seconds)
    assert(result=="1 3\n567")
  }

  "utfSimplify" should "replace hyphens and quotes, strip controls and reduce whitespace" in {
    import Quote._
    import White._
    val input = s"1${sp}${nb}3${nl}${cr}5\u001e6\u00807${singleCurlyLeft}8${singleCurlyRight}9${doubleCurlyLeft}10${doubleCurlyRight}11${Hyphen.rgx_hyphens}12"
    val future = testSource(input) via Cleaning.Pipeline.utfSimplify runWith testSink
    val result = Await.result(future, 3 seconds)
    assert(result=="1 3\n567'8'9\"10\"11-|-|-|-|-|-|-|-12")
  }

  "asciiOnly" should "replace or strip all non-ascii characters" in {
    import Quote._
    import White._
    val input = s"1${sp}${nb}3${nl}${cr}5\u001e6\u00807${singleCurlyLeft}8${singleCurlyRight}9${doubleCurlyLeft}10${doubleCurlyRight}11${Hyphen.rgx_hyphens}12"
    val future = testSource(input) via Cleaning.Pipeline.asciiOnly runWith testSink
    val result = Await.result(future, 3 seconds)
    assert(result=="1 3\n567'8'9\"10\"11-|-|-|-|-|-|-|-12")
  }

}
