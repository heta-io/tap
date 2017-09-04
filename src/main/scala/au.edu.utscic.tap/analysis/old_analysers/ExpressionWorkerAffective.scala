package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */

import akka.actor.{Actor, ActorLogging}

class ExpressionWorkerAffective extends Actor with ActorLogging{

//  val lexicon = AffectLexicon.load
//  log.debug("Affect lexicon loaded with {} words.",lexicon.size)
//  val mostEmotional = lexicon.filter(_.arousal> 4.9) //3.75)
//  log.debug("Most emotional words: {}",mostEmotional.size)
//  val mostEmotionalSorted = mostEmotional.sortBy(_.valence)
//  val subSetSize = (1 * mostEmotional.size)/10 //closed int value 10% of the set
//  val negative = mostEmotionalSorted.take(subSetSize)
//  val negValenceValues = negative.map(_.valence)
//  val positive = mostEmotionalSorted.takeRight(subSetSize)
//  val posValenceValues = positive.map(_.valence)
//  log.debug("Negative words: {} - {} to {}",negative.size,negValenceValues.min,negValenceValues.max)
//  log.debug("Positive words: {} - {} to {}",positive.size,posValenceValues.min,posValenceValues.max)

  override def receive = {
    //case annotations:Annotations => sender ! affective(annotations.tapAnnotations,annotations.paraIndex)
    case _ => log.error("ExpressionWorkerAffective received an unknown message!")
  }
/*
  def affective(annotations:List[(TapAnnotation,Int)],paraIndex:Int):AffectExpressions = {
    log.debug("Affect analysis for paragraph {}",paraIndex)
    val paraWords = annotations.map(_._1.word.toLowerCase)

    //val top40arousal = lexicon.sortBy(w => w.arousal)
    val posWords = positive.filter(w => paraWords.contains(w.word))
    val negWords = negative.filter(w => paraWords.contains(w.word))
    val affectWords = posWords ++ negWords
    val affectCount = affectWords.size
    log.debug("Number of affect words: {} out of {}",affectCount,paraWords.size)
    val affectPercent = (affectCount*100)/paraWords.size.toDouble
    val affectRating = if(affectPercent> 2) "strong"
    else if(affectPercent > 1) "moderate"
    else "weak"
    log.debug("Affect %: {} [{}]",affectPercent,affectRating)
    log.debug("Number of positive words: {} out of {}",posWords.size,paraWords.size)
    log.debug("Number of negative words: {} out of {}",negWords.size,paraWords.size)
    log.debug("Affect words by dominance: {}",affectWords.sortBy(_.dominance).map(w => w.word+" ("+w.dominance+")").mkString(", "))
    val expressions = affectWords.filter(_.dominance > 6).map(w =>Expression("EMOTION",w.word,0,0))
    AffectExpressions(affectRating,expressions)
  }
*/
}
/*
object AffectLexicon {
  implicit val formats: Formats = DefaultFormats
  implicit val jacksonSerialization: Serialization = jackson.Serialization
  val fileName = "/affect-lexicon.json"
  lazy val stream : InputStream = getClass.getResourceAsStream(fileName)
  lazy val src = scala.io.Source.fromInputStream( stream )
  def load:List[Affect] = org.json4s.jackson.Serialization.read[List[Affect]](src.reader())
}
*/
case class Affect(word:String,valence:Double,arousal:Double,dominance:Double)