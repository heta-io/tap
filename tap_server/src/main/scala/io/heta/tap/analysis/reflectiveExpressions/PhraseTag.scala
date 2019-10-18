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

package io.heta.tap.analysis.reflectiveExpressions

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * This object holds all of the phraseTag data as well as necessary pattern rules
 * and filter methods
 *
 * Created by Andrew Gibson on 3/07/15.
 *
 */

/**
  * A bag of phrases that can be used to extract finer meaning
  * Phrase tags defines the structural meaning of a block of text or semantics of text
  * Phrase tags used such as outcome, temporal, pertains, consider, anticipate ..etc
  */

object PhraseTag {

  val posPattern = List(
    //Prosessive pronoun
    (("startRepeat", List("PRP$", "JJ", "NN")), List("selfPossessive", "groupPossessive", "othersPossessive")),
    //Pronoun
    (("startRepeat", List("PRP", "RB", "VB", "CC", "IN")), List("consider", "anticipate", "emotive", "generalPronounVerb")),
    //Preposition
    (("startRepeat", List("IN", "PRP", "JJ", "NN", "VB", "MD", "PDT", "W", "TO")), List("compare", "temporal", "pertains", "manner", "outcome", "generalPreposition")),
    //Modal
    (("startRepeat", List("MD", "RB", "VB")), List("definite", "possible")),
    //Reflexive
    (("startEnd", List("PRP", "PRP")), List("selfReflexive", "groupReflexive"))
  )

  val termFilter: mutable.Map[String, (String, AnyRef)] = mutable.HashMap(
    //Possessive
    "selfPossessive" ->("startsWithAny", List("my")),
    "groupPossessive" ->("startsWithAny", List("our")),
    "othersPossessive" ->("startsWithAny", List("their", "his", "her")),
    //Pronoun
    "consider" ->("containsAny", List("feel", "felt", "seem", "think", "realise", "being", "thought", "decid", "know", "appeared", "learn", "experience", "focus", "found", "guess", "believe", "wonder", "find", "personal", "reflect", "brain", "understand", "understood", "notice", "myself", "recent", "imply", "record", "probably", "emotion", "physical", "trust", "deal", "barely", "pretty", "extremely", "incredibly", "miraculously", "supposed")),
    "anticipate" ->("containsAny", List("looking forward", "start", "motivat", "follow", "need", "plan", "keep", "balance", "potential", "try", "trie", "turn", "attempt", "improve", "inspire", "develop", "assure", "establish", "connect", "engage", "fix")),
    "emotive" ->("containsAny", List("struggl", "never", "push", "carry", "scrambl", "going down", "disappoint", "distress", "whin", "tear", "cry", "chas", "slog", "negative", "challeng", "gradual", "neglect", "deflate", "drain", "freak", "soar", "well", "joy", "lov", "reliev", "positive", "accomplish", "energize", "energise")),
    "generalPronounVerb" ->("containsNone", List()), // This is filterNot of apvl
    //Preposition
    "compare" ->("startsWithAny", List("whereas", "besides", "despite", "but", "although", "than", "though", "however", "like", "unlike")),
    "temporal" ->("startsWithAny", List("while", "once", "during", "upon", "after", "since", "towards", "until", "before")),
    "pertains" ->("startsWithAny", List("across", "to", "of", "that", "on", "among", "about", "under", "over", "with ", "within ", "around", "whether", "for", "in", "eg", "ie")),
    "manner" ->("startsWithAny", List("via", "by", "through", "without")),
    "outcome" ->("startsWithAny", List("because", "as", "cause", "from", "so")),
    "generalPreposition" ->("startsWithNone", List()), // This is filterNot of all other prep lists
    //Modal
    "definite" ->("startsWithAny", List("will", "'ll", "ca")),
    "possible" ->("startsWithAny", List("might", "may", "could", "would", "should")),
    //Reflexive
    "selfReflexive" ->("startsAndEndsWithAny", (List("i ", "my", "me"), List("i", "self", "me", "my"))),
    "groupReflexive" ->("startsAndEndsWithAny", (List("we", "our"), List("ourselves", "ourself", "our", "us")))
  )

  private val generalPronoun = List("consider", "anticipate", "emotive").map(termFilter.apply(_).asInstanceOf[(String, List[String])]._2).reduce(_ ++ _)
  termFilter += "generalPronoun" ->("containsNone", generalPronoun)

  private val generalPreposition = List("compare", "temporal", "pertains", "manner", "outcome").map(termFilter.apply(_).asInstanceOf[(String, List[String])]._2).reduce(_ ++ _)
  termFilter += "generalPreposition" ->("containsNone", generalPreposition)

  /**
    * Filter a list of phrases for finer grained meaning.
    *
    * @param phraseTag Type of Phrase
    * @param phrase Phrase
    * @return phrases for finer grained meaning
    */
  def filter(phraseTag:String,phrase:String) = {
    if(phraseTag.contains("Reflexive")) { // This type has 2 lists - one for the start and a different one for the end
    val filterData = PhraseTag.termFilter.apply(phraseTag).asInstanceOf[(String,(List[String],List[String]))]
      val startList = filterData._2._1
      val endList = filterData._2._2
      startList.map(phrase.startsWith(_)).reduce(_ || _) && endList.map(phrase.endsWith(_)).foldLeft(false)(_ || _)
    } else {
      val filterData = PhraseTag.termFilter.apply(phraseTag).asInstanceOf[(String,List[String])]
      val filterType = filterData._1
      val filterList = filterData._2
      if (filterType.contentEquals("startsWithAny")) filterList.map(phrase.startsWith(_)).foldLeft(false)(_ || _)
      else if(filterType.contentEquals("startsWithNone")) filterList.map(!phrase.startsWith(_)).forall(tf => tf)
      else if(filterType.contentEquals("containsAny")) filterList.map(phrase.contains(_)).foldLeft(false)(_ || _)
      else if(filterType.contentEquals("containsNone")) filterList.map(!phrase.contains(_)).forall(tf => tf)
      else false
    }
  }

  /**
    * Subordinate to phraseTag
    *
    * @param phraseTags Type of Phrase
    * @return
    */
  def subTags(phraseTags:Seq[String]):Seq[String] = {
    val mcTags = ArrayBuffer[String]()
    if(phraseTags.contains("outcome")) mcTags += "trigger"
    if (phraseTags.contains("temporal") || (phraseTags.contains("pertains") && phraseTags.contains("consider"))) mcTags += "monitorControl"
    if (phraseTags.contains("anticipate") || phraseTags.contains("definite") || phraseTags.contains("possible")) mcTags += "goal"
    if(phraseTags.contains("selfReflexive") || phraseTags.contains("emotive")) mcTags += "experience"
    if (phraseTags.contains("selfPossessive") || phraseTags.contains("compare") || phraseTags.contains("manner")) mcTags += "knowledge"
    mcTags.distinct
  }

  /**
    * Meta information for the subTags
    *
    * @param subTags Subordinate to phraseTag
    * @return meta information about the subTags
    */
  def metaTags(subTags:Seq[String]):Seq[String] = {
    val mTags = ArrayBuffer[String]()
    if (subTags.contains("monitorControl") && (subTags.contains("trigger") || subTags.contains("goal"))) mTags += "regulation"
    if (subTags.contains("experience")) mTags += "experience"
    if (subTags.contains("knowledge")) mTags += "knowledge"
    mTags.distinct
  }

  /**
    * Ratio between the self-count and total-count
    *
    * @param phraseTags Type of Phrase
    * @return selfCount/totalCount if Seq[String] length > 0
    */
  def selfRatio(phraseTags:Seq[String]):Double = {
    var selfCount = 0.0
    val totalCount = phraseTags.length
    if(phraseTags.contains("selfPossessive")) selfCount += 1.0
    if(phraseTags.contains("selfReflexive")) selfCount += 1.0

    if(totalCount>0) selfCount / totalCount
    else -1.0
  }

  /**
    * Ratio between the other-count and total-count
    *
    * @param phraseTags Type of Phrase
    * @return othersCount/totalCount if Seq[String] length > 0
    */
  def othersRatio(phraseTags:Seq[String]):Double = {
    var othersCount = 0.0
    val totalCount = phraseTags.length
    if(phraseTags.contains("groupPossessive")) othersCount += 1.0
    if(phraseTags.contains("othersPossessive")) othersCount += 1.0
    if(phraseTags.contains("groupReflexive")) othersCount += 1.0

    if(totalCount>0) othersCount / totalCount
    else -1.0
  }
}