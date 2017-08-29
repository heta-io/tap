package au.edu.utscic.tap.pipelines

import akka.NotUsed
import akka.stream.scaladsl.Flow
import au.edu.utscic.tap.data.StringTypes.{DocumentStr, SectionStr}

/**
  * Created by andrew@andrewresearch.net on 30/6/17.
  */
object Sectioning {

  object Pipeline {
    val singleNewline:Flow[Char,SectionStr,NotUsed] = makeString.via(docSplit)
  }

  val makeString:Flow[Char,String,NotUsed] = Flow[Char].fold("")(_ + _)

  val docSplit:Flow[DocumentStr,SectionStr,NotUsed] = Flow[DocumentStr].map(_.split('\n').toList).mapConcat(identity)
}
