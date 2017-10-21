package tap.services.analytics.aggregators

import akka.actor.{Actor, ActorLogging}
import akka.pattern.ask
import akka.util.Timeout
import tap.data._

import scala.concurrent.Future
import scala.concurrent.duration._


class ComplexityAggregator extends Actor with ActorLogging {

  implicit val executor = context.system.dispatcher
  implicit val timeout = Timeout (30 seconds)
//  val metricsAnalyser = context.actorSelection("/user/analyticsService/metricsAnalyser")
//  val vocabAnalyser = context.actorSelection("/user/analyticsService/vocabAnalyser")
//  val syllableAnalyser = context.actorSelection("/user/analyticsService/syllableAnalyser")
//  val posAnalyser = context.actorSelection("/user/analyticsService/posAnalyser")
//  val complexityAnalyser = context.actorSelection("/user/analyticsService/complexityAnalyser")

  override def preStart() = {
    log.debug("Starting Aggregator")
  }
  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.error(reason, "Restarting Aggregator due to [{}] when processing [{}]",
      reason.getMessage, message.getOrElse(""))
  }

  def receive = {
//    case text:String => sender ! aggregate(text)
//    case inputData:InputData => sender ! aggregate(inputData)
    case _ => sender ! "Invalid input"
  }
/*
  def aggregate(text:String):Future[AllComplexity] = aggregate(InputData("","",text.split("\n").toList))

  def aggregate(inputData:InputData):Future[AllComplexity] = {

    for {
      am <- ask(metricsAnalyser, inputData).mapTo[Future[AllMetrics]].flatMap(identity)
      av <- ask(vocabAnalyser, inputData).mapTo[Future[AllVocab]].flatMap(identity)
      as <- ask(syllableAnalyser,inputData).mapTo[Future[AllSyllables]].flatMap(identity)
      ac = complexity(am,av,as)
    } yield ac
  }

  private def complexity(metrics:AllMetrics,vocab:AllVocab,syllables:AllSyllables):AllComplexity = {
    val docComplexity = {
      calcComplexity(metrics.documentMetrics,vocab.documentVocab,syllables.documentSyllables)
    }
    val paraComplexity:List[ParagraphComplexity] = metrics.paragraphMetrics.zipWithIndex.map { case (m,i) =>
      val v = vocab.paragraphVocab(i)
      val s = syllables.paragraphSyllables(i)
      ParagraphComplexity(i+1,calcComplexity(m,v,s))
    }
    AllComplexity(docComplexity,paraComplexity)
  }

  def calcComplexity(m:Metrics,v:Vocab,s:Syllables):GenericComplexity = {
    val selectVocab = v.countVocab.map(_._2).flatten.filterNot(_.length < 4).toList
    val vocabToDocRatio = selectVocab.length / m.wordCount.toDouble
    val avgSentLength = m.wordCount / m.sentenceCount.toDouble
    val avgWordLength = m.characterCount / m.wordCount.toDouble
    val avgSyllables = s.averageSyllables
    GenericComplexity(vocabToDocRatio,avgSentLength,avgWordLength,avgSyllables)
  }
  */

}