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

//package tap.services.analytics.aggregators

/*
  def aggregate(text:String):Future[AllComplexity] = aggregate(InputData("","",text.split("\n").toList))

  def aggregate(inputData:InputData):Future[AllComplexity] = {

    for {
      am <- ask(metricsAnalyser, inputData).mapTo[Future[AllMetrics]].flatMap(identity)
      av <- ask(vocabAnalyser, inputData).mapTo[Future[AllVocab]].flatMap(identity)
      as <- ask(syllableAnalyser,inputData).mapTo[Future[AllSyllables]].flatMap(identity)
      ac = complexity(am,av,as)
    } yield ac
  }

  private def complexity(metrics:AllMetrics,vocab:AllVocab,syllables:AllSyllables):AllComplexity = {
    val docComplexity = {
      calcComplexity(metrics.documentMetrics,vocab.documentVocab,syllables.documentSyllables)
    }
    val paraComplexity:List[ParagraphComplexity] = metrics.paragraphMetrics.zipWithIndex.map { case (m,i) =>
      val v = vocab.paragraphVocab(i)
      val s = syllables.paragraphSyllables(i)
      ParagraphComplexity(i+1,calcComplexity(m,v,s))
    }
    AllComplexity(docComplexity,paraComplexity)
  }

  def calcComplexity(m:Metrics,v:Vocab,s:Syllables):GenericComplexity = {
    val selectVocab = v.countVocab.map(_._2).flatten.filterNot(_.length < 4).toList
    val vocabToDocRatio = selectVocab.length / m.wordCount.toDouble
    val avgSentLength = m.wordCount / m.sentenceCount.toDouble
    val avgWordLength = m.characterCount / m.wordCount.toDouble
    val avgSyllables = s.averageSyllables
    GenericComplexity(vocabToDocRatio,avgSentLength,avgWordLength,avgSyllables)
  }
  */
