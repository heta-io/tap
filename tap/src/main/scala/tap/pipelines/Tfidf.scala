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

package tap.pipelines

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Zip}
import tap.analysis.textshapes.TfIdfShape
import tap.data.{OldTapDocument, OldTapSection}
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
