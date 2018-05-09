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

package tap.analysis.textshapes

import io.nlytx.commons.ranking.TfIdf

/** A shape for comparing engineering documents
  * ===Purpose===
  * This shape is a basic template that splits a document based on newline characters
  * and measures intensity by vocabulary (TF/IDF)
  *
  *  @constructor create a new engineeringShape with a set split pattern.
  */
object TfIdfShape extends AbstractShape {

  override val splitPattern: String = "[\n]+"

  /** Split the document into chunks
    *  @example {{{val chunkedString = chunk("my string\n split in two")}}}
    *  @param doc the string that is to be split
    *  @return a list of strings
    */
  override def chunk(doc:String):List[String] = doc.split(splitPattern).toList.filterNot(s => s.isEmpty || s.matches("[\\s]+"))


  /** Produce a distribution for a list of chunks
    *
    * @param chunks the list of stra
    * @param findIntensities
    * @return
    */
  def density(chunks: List[String], findIntensities: (List[String],Boolean,Double) => List[Map[String,Double]]): List[Double] = {

    //val intensities = Tfidf.calculateWeighted(chunks,false)
    val intensities = findIntensities(chunks,true,0.5) // true = ranked, 0.5 take top 50%
    val words = TfIdf.docWords(chunks)

    val sizes =  words.map(_.size)

    val wordRanks = words.zipWithIndex.map { d=>
      val values = intensities(d._2)
      val words = d._1
      words.map( w => values.getOrElse(w,0.0))
    }

    wordRanks.reduce( (m,n) => m ++ n)

  }

  /** Compare a test distribution to a reference distribution
    * Compares two distribution and returns their similarity from 0 (none) to 1 (match)
    * @todo Still to be implemented
    * @param testDistribution The distribution to find the similarity of
    * @param referenceDistribution The distribution that is the benchmark or reference to compare to
    * @return a similarity measure of type Double that ranges from 0 (no similarity) to 1 (exact match)
    */
  override def compare(testDistribution: List[Double], referenceDistribution: List[Double]): Double = {
    //TODO This is a stub - needs to be implemented
    0.0
  }
}

