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

//package tap.services.feedback

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */


/*
  def sentenceLevelFeedback(fbData:Future[FeedbackRequestData]):Future[List[AwaOutputData]] = {
    for { data <- fbData } yield sentenceXipFB(data)
  }

  def sentenceXipFB(feedbackData:FeedbackRequestData):List[AwaOutputData] = {
    val xipData = feedbackData.analytics.documentAnalytics.xip.sentences
    val startParas: List[String] = feedbackData.inputData.text
    val paras: List[String] = startParas.filterNot(p => p.trim.isEmpty)
    val removed = startParas.length - paras.length
    if (removed > 0) log.warning("Removed {} empty paragraphs", removed)
    var i = 0
    val indexedResponses = xipData.map { resp =>
      if (resp.AWAPARA) i = i + 1
      (resp, i)
    }
    indexedResponses
      .filterNot(_._1.REFLCONC.isEmpty)
      .filterNot(_._1.REFLCONC_M.isEmpty)
      .map(sentenceFeedback(_, paras))
  }

  private def sentenceFeedback(resp:(SentenceXip,Int),paras:List[String]):AwaOutputData = {
    //val cleaner = new TextCleaning()
    //def clean(t:String) = cleaner.clean(t)
    //def escape(t:String) = cleaner.htmlEscape(t)
    //def showInvisibles(t:String) = cleaner.showInvisibles(t,"@")
    //def codes(t:String) = t.map(cleaner.charCode(_))

    val paraIndex = resp._2-1
    val numParas = paras.length
    //log.debug("Para: "+(paraIndex+1))
    //log.debug("Number of REFLCONC: "+resp._1.REFLCONC.length)
    val sent = resp._1.REFLCONC.head
    val start = paras(paraIndex).indexOf(sent)
    if(start == -1) {
      log.error("Sentence NOT found: |"+sent+"|") //+showInvisibles(sent)+"|")
      log.debug("Looking in para "+paraIndex+" of "+numParas) //: "+showInvisibles(paras(paraIndex))+"|")
      AwaOutputData("ERROR","XIP Sentence not found in original text",List(sent),paraIndex,paraIndex,start,start)
      //log.info("Trying with clean paragraph...")
      //val start2 = paras(paraIndex).replaceAll("(\u0026\u0020)","").indexOf(sent)
//      val start2 = clean(paras(paraIndex)).indexOf(clean(escape(sent)))  //TODO Escaping the text is a hack as XIP is not returning encoded text. This can be removed when XIP encodes the text
//      if(start2 == -1) {
//        log.error("Sentence NOT found in clean paragraph - no more options")
//        AwaOutputData("ERROR","XIP Sentence not found in original text",List(showInvisibles(sent)),paraIndex,paraIndex,start,start)
//      } else {
//        log.warning("Sentence found in clean paragraph - may not match original text")
//        val end2 = start2 + sent.length
//        val tags = resp._1.REFLCONC_M.head
//        AwaOutputData("annotation", "Reflection Concepts - WARNING: Unclean paragraph", tags, resp._2, resp._2, start2, end2)
//      }
    } else {
      val end = start + sent.length
      AwaOutputData("annotation", "Reflection Concepts", resp._1.REFLCONC_M.head, resp._2, resp._2, start, end)
    }
  }
  */

