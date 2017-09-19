package au.edu.utscic.tap.pipelines

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Framing, Source}
import akka.util.ByteString
import au.edu.utscic.tap.data.StringTypes.{DocumentStr, SectionStr}
import au.edu.utscic.tap.nlp.{NlpBuilders, NlpDocument, OldNlpSentence}
import au.edu.utscic.tap.data.{OldTapDocument, OldTapSection, OldTapSentence, OldTapTags}

/**
  * Created by andrew@andrewresearch.net on 24/2/17.
  */
object Parsing_OpenNLP {

  object Pipeline {
    /** A stream of document strings split into sections on newline,
      *  which are treated as NLP Documents and then used to produce
      *  TapSections that are folded into a OldTapDocument
      */
    val default:Flow[SectionStr,OldTapDocument,NotUsed] = nlpDocs.via(nlpSentences).via(tapSentences).via(tapSection).via(tapDocument)


  }

  import au.edu.utscic.tap.nlp.openNlp.OpenNlpImplicits._





  val nlpDocs:Flow[SectionStr,NlpDocument,NotUsed] = Flow[String].map(s => NlpBuilders.document(s))

  val nlpSentences:Flow[NlpDocument,List[OldNlpSentence],NotUsed] = Flow[NlpDocument].map(_.sentences)

  val tapSentences:Flow[List[OldNlpSentence],List[OldTapSentence],NotUsed] = Flow[List[OldNlpSentence]].map(_.map(s => buildTapSentence(s)))

  val tapSection:Flow[List[OldTapSentence],OldTapSection,NotUsed] = Flow[List[OldTapSentence]].map(OldTapSection(_))

  val tapDocument = Flow[OldTapSection].fold(List[OldTapSection]())(_:+_).map(OldTapDocument(_))



  def buildTapSentence(sentence:OldNlpSentence) = OldTapSentence(
    sentence.text,
    sentence.words.map(_.toLowerCase),
    OldTapTags(
      sentence.lemmas,
      sentence.posTags
    )
  )


}


