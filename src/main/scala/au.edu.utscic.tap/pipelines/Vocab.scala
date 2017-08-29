package au.edu.utscic.tap.pipelines

import akka.NotUsed
import akka.stream.scaladsl.Flow
import au.edu.utscic.tap.data.{TapDocument, TapSection}

import scala.collection.immutable.{ListMap, SortedMap}
import scala.collection.mutable
import scala.concurrent.Future

/**
  * Vocab
  * Created by andrew@andrewresearch.net on 27/2/17.
  */
object Vocab {

  val document:Flow[TapDocument,List[TapSection],NotUsed] = Flow[TapDocument].map(_.sections)

  val sectionsVocab:Flow[List[TapSection],List[Map[String,Int]],NotUsed] = Flow[List[TapSection]].map(_.map(_.sentences.flatMap(_.tokens).groupBy((word:String) => word).mapValues(_.length)))
  val documentVocab = Flow[List[Map[String,Int]]].fold(Map[String,Int]())(_ ++ _.flatten)

  val vocabByCount = Flow[Map[String,Int]].map(_.toList.groupBy(_._2).map(wc => wc._1 -> wc._2.map(_._1)).toSeq.reverse).map(l=> ListMap(l:_*))

  val pipeline = document.via(sectionsVocab).via(documentVocab).via(vocabByCount)

}
