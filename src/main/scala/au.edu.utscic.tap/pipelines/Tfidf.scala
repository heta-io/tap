package au.edu.utscic.tap.pipelines

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Zip}
import au.edu.utscic.tap.analysis.textshapes.TfIdfShape
import au.edu.utscic.tap.data.{TapDocument, TapSection}
import io.nlytx.commons.ranking.TfIdf

import scala.collection.immutable.ListMap
import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 27/2/17.
  */
object Tfidf {

  val pipeline:Flow[List[String],List[Map[String,Double]],NotUsed] = Flow[List[String]].map(TfIdf.calculateNonWeighted(_))

  /*
  val termsInDocs = Flow[List[String]].map(_.map( d => TfIdf.rawTermFrequency(d)))

  val idf = Flow[List[Map[String,Long]]].map(_.map( m => TfIdf.inverseDocFrequency(m ,m.size)))

  val corpusPipeline = termsInDocs.via(idf)

  val wtf = Flow[String].map(TfIdf.weightedTermFrequency(_))

  val tfIdf = Zip[Map[String,Double],Map[String,Double]] //.zipper((v1:Map[String,Double],v2:Map[String,Double]) => TfIdf.tfIdf(v1,v2))
*/

}
