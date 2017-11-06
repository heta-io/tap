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
  def documentLevelFeedback(fbData:Future[FeedbackRequestData]):Future[List[AwaOutputData]] = {
    for {
      data <- fbData
      moves <- Future(rhetoricalMoveFB(data))
      spelling <- Future(docSpellingFB(data))
      complexity <- Future(docComplexityFB(data))
    } yield moves ++ spelling ++ complexity
  }

  def rhetoricalMoveFB(feedbackData:FeedbackRequestData):List[AwaOutputData] = {

    val moveCounts =  countMoves(feedbackData.analytics.documentAnalytics.xip.sentences)
    val sentenceCount = feedbackData.analytics.documentAnalytics.metrics.sentenceCount
    val comment = if(moveCounts.oneOrMoreZero) RhetoricalMovesText.docMissingMoves
    else if(moveCounts.unBalanced) RhetoricalMovesText.docImbalance
    else if(moveCounts.tooFew(sentenceCount)) RhetoricalMovesText.docFewMoves
    else if(moveCounts.tooMany(sentenceCount)) RhetoricalMovesText.docTooMany
    else RhetoricalMovesText.default
    List(AwaOutputData("text","rhetorical moves",List(comment),0,0,0,0))
  }

  def countMoves(sentences:List[SentenceXip]):MoveCounts = {
    val moves:List[String] = sentences.filterNot(s => s.REFLCONC.isEmpty).filterNot(s => s.REFLCONC_M.isEmpty).map(_.REFLCONC_M).flatten.flatten
    val context = moves.count(_.contains("CONTEXT"))
    val challenge = moves.count(_.contains("CHALLENGE"))
    val change = moves.count(_.contains("CHANGE"))
    log.info("Document rhetorical move counts: [Context]: {}, [Challenge]: {}, [Change]: {}",context,challenge,change)
    MoveCounts(context,challenge,change)
  }

  case class MoveCounts(context:Int, challenge:Int, change:Int) {
    def oneOrMoreZero:Boolean = (context==0 || challenge==0 || change==0)
    def unBalanced:Boolean = ( context > (challenge+change) || challenge > (context+change) || change > (challenge+ context))
    def tooFew(sentenceCount:Int):Boolean = (sentenceCount > 4*(context + challenge + change))
    def tooMany(sentenceCount:Int):Boolean = (sentenceCount < 1.2*(context + challenge + change))
  }

  def docSpellingFB(feedbackData:FeedbackRequestData):List[AwaOutputData] = {
    val analytics = feedbackData.analytics
    val inputData = feedbackData.inputData
    val spellCount = analytics.documentAnalytics.spelling.spelling.map(_.spelling.errorCounts.map(_._2).sum).sum

    val neCount = analytics.documentAnalytics.posStats.namedEntities.size
    val errorCount = if(neCount > spellCount) 0 else spellCount - neCount
    log.info("Doc Spell Count: {}, NE Count: {} Error Count: {}",spellCount,neCount,errorCount)
    val parasCount = inputData.text.mkString(" ").split(" ").length
    val errorPercent = errorCount.toDouble / parasCount
    //log.debug("Error percent: "+errorPercent)
    val spellComment = if(errorPercent<0.005) {
      SpellText.docVeryFewErrors
    } else if(errorPercent<0.03) {
      SpellText.docSomeErrors(spellCount)
    } else if(errorPercent<0.1) {
      SpellText.docQuiteAlotErrors(spellCount)
    } else {
      SpellText.docTooManyErrors(spellCount)

    }
    List(AwaOutputData("text","spelling",List(spellComment),0,0,0,0))
  }

  def docComplexityFB(feedbackData:FeedbackRequestData):List[AwaOutputData] = {
    val c = feedbackData.analytics.documentAnalytics.complexity
    val sy = c.avgSyllables
    val wl = c.avgWordLength
    val vo = c.vocabToDocRatio
    val co = sy * wl * vo
    val complexityText = f"The complexity value for this document is $co%1.3f [SY: $sy%1.3f] [WL: $wl%1.3f] [VO: $vo%1.3f]"
    List(AwaOutputData("value","complexity",List(complexityText),0,0,0,0))
  }

*/

