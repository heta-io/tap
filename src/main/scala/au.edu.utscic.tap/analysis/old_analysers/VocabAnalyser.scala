package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 13/07/2016.
  */

class VocabAnalyser extends Analyser {

 /* import context._

  override def preStart() = {
    log.debug("Starting VocabAnalyser")
  }
  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.error(reason, "Restarting VocabAnalyser due to [{}] when processing [{}]",
      reason.getMessage, message.getOrElse(""))
  }
*/

  override def receive = {
//    case text:String => sender ! vocab(text)
//    case text:InputData => sender ! vocab(text)
//    case request:ParagraphRequest => sender ! paragraphVocab(request)
//    case request:DocumentRequest => sender ! documentVocab(request)
    case _ => sender ! "Invalid input"
  }

/*
  def vocab(text:String):Future[AllVocab] = {
    val paras = text.split("\n").toList
    vocab(InputData("","",paras))
  }
  def vocab(inputData:InputData):Future[AllVocab] = {
    val paras = inputData.text
    val pvs = paras.zipWithIndex.map { case (para, idx) =>
      val request = ParagraphRequest(idx + 1, para) //paragraph indexes start at 1
      paragraphVocab(request)
    }
    for (dv <- documentVocab(pvs)) yield AllVocab(dv, pvs)
  }

  def paragraphVocab(request:ParagraphRequest):ParagraphVocab = {
    //log.debug("Calculating ParagraphVocab for paragraph {}",request.index)
    val words = TextSplit.wordSplit(request.text).map(_.toLowerCase)
    val wordCounts:Map[String,Int] =  words.groupBy((word:String) => word).mapValues(_.length)
    val countVocab = wordCounts.toList.groupBy(_._2).map( wc => wc._1 -> wc._2.map(_._1))
    ParagraphVocab(request.index,countVocab,wordCounts)
  }

  def documentVocab(paras:List[ParagraphVocab]):Future[DocumentVocab] = {
    for {
      wordCounts <- Future(paras.map(_.wordCounts).map(_.toList).flatten.groupBy(_._1).mapValues(_.map(_._2).sum))
      countVocab <- Future(wordCounts.toList.groupBy(_._2).map( wc => wc._1 -> wc._2.map(_._1)))
    } yield DocumentVocab(countVocab)
  }

  def documentVocab(request:DocumentRequest):Future[DocumentVocab] = {
    //log.debug("Calculating DocumentVocab for paragraphs")
    for {
      pv <- request.paragraphs.flatMap(pas => Future(pas.map(_.vocab)))
      dv <- documentVocab(pv)
    } yield dv
  }

*/

}
