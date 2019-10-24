package views

import org.clulab.odin._
import org.clulab.processors.{Document, Sentence}

/**
  * Created by Carlos Agelvis on 10/10/2019.
  */

object Test extends GenericPage {
  var so = ""

  def displayMentions(mentions: Seq[Mention], doc: Document): String = {
    val mentionsBySentence = mentions groupBy (_.sentence) mapValues (_.sortBy(_.start)) withDefaultValue Nil
    for ((s, i) <- doc.sentences.zipWithIndex) {
      so = s"sentence #$i"
      println(s.getSentenceText)

      val sortedMentions = mentionsBySentence(i).sortBy(_.label)
      val (events, entities) = sortedMentions.partition(_ matches "Event")
      println
      println("events:")
      events foreach displayMention
      println("=" * 50)
    }

    "Please check results on console"
  }

  def printSyntacticDependencies(s:Sentence): Unit = {
    if(s.dependencies.isDefined) {
      println(s.dependencies.get.toString)
    }
  }

  def displayMention(mention: Mention) {
    val boundary = s"\t${"-" * 30}"
    println(s"${mention.labels} => ${mention.text}")
    println(boundary)
    println(s"\tRule => ${mention.foundBy}")
    val mentionType = mention.getClass.toString.split("""\.""").last
    println(s"\tType => $mentionType")
    println(boundary)
    mention match {
      case tb: TextBoundMention =>
        println(s"\t${tb.labels.mkString(", ")} => ${tb.text}")
      case em: EventMention =>
        println(s"\ttrigger => ${em.trigger.text}")
        displayArguments(em)
      case rel: RelationMention =>
        displayArguments(rel)
      case _ => ()
    }
    println(s"$boundary\n")
  }


  def displayArguments(b: Mention): Unit = {
    b.arguments foreach {
      case (argName, ms) =>
        ms foreach { v =>
          println(s"\t$argName ${v.labels.mkString("(", ", ", ")")} => ${v.text}")
        }
    }
  }

}
