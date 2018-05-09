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

/** The generic shape pattern requiring specific implementation
  *
  * @todo Potential intensity functions…
  *
  * 1. The number of important sections words in a nlpSentences. i.e. TF/IDF (sections as collection, chunk as sections), count of most rare words.
  * 2. Vocabulary - count of unique words per nlpSentences
  * 3. Length - size of nlpSentences
  * 4. Word2Vec similarity
  * 5. Cosine similarity of centroid
  *
  */
abstract class AbstractShape {

  val splitPattern:String = "[\n]+"

  //Split the document into chunks
  def chunk(doc:String):List[String] = doc.split(splitPattern).toList

  //Return a distribution (list of normalised values) for a list of chunks
  def density(chunks:List[String],findIntensities: (List[String],Boolean,Double) => List[Map[String,Double]]):List[Double]
  //intensity:(String,Option[String]) => Double):List[Double]

  // The degree of similarity from 0 (none) to 1 (match)
  def compare(testDistribution:List[Double],referenceDistribution:List[Double]): Double

}
