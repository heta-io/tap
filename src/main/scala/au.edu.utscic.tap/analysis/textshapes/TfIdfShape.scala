package au.edu.utscic.tap.analysis.textshapes

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

