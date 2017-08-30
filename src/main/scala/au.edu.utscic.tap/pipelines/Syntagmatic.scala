package au.edu.utscic.tap.pipelines

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Framing, Source}
import akka.util.ByteString
import au.edu.utscic.tap.data.StringTypes.{DocumentStr, SectionStr}
import au.edu.utscic.tap.nlp.{NlpBuilders, NlpDocument, NlpSentence}
import au.edu.utscic.tap.data.{TapDocument, TapSection, TapSentence, TapTags}

/**
  * Created by andrew@andrewresearch.net on 24/2/17.
  */
object Syntagmatic {

  object Pipeline {
    /** A stream of document strings split into sections on newline,
      *  which are treated as NLP Documents and then used to produce
      *  TapSections that are folded into a TapDocument
      */
    val default:Flow[SectionStr,TapDocument,NotUsed] = nlpDocs.via(nlpSentences).via(tapSentences).via(tapSection).via(tapDocument)


  }

  import au.edu.utscic.tap.nlp.openNlp.OpenNlpImplicits._





  val nlpDocs:Flow[SectionStr,NlpDocument,NotUsed] = Flow[String].map(s => NlpBuilders.document(s))

  val nlpSentences:Flow[NlpDocument,List[NlpSentence],NotUsed] = Flow[NlpDocument].map(_.sentences)

  val tapSentences:Flow[List[NlpSentence],List[TapSentence],NotUsed] = Flow[List[NlpSentence]].map(_.map(s => buildTapSentence(s)))

  val tapSection:Flow[List[TapSentence],TapSection,NotUsed] = Flow[List[TapSentence]].map(TapSection(_))

  val tapDocument = Flow[TapSection].fold(List[TapSection]())(_:+_).map(TapDocument(_))



  def buildTapSentence(sentence:NlpSentence) = TapSentence(
    sentence.text,
    sentence.words.map(_.toLowerCase),
    TapTags(
      sentence.lemmas,
      sentence.posTags
    )
  )


}


