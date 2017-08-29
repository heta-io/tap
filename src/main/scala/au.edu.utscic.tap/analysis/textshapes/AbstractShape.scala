package au.edu.utscic.tap.analysis.textshapes

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
