package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */

import akka.actor.{Actor, ActorLogging}

class ExpressionWorkerModal extends Actor with ActorLogging{

  override def receive = {
    //case annotations:Annotations => sender ! modal(annotations.tapAnnotations,annotations.paraIndex)
    case _ => log.error("ExpressionWorkerModal received an unknown message!")
  }
/*
  def modal(annotations:List[(TapAnnotation,Int)],paraIndex:Int):List[Expression] = {
    val modals = annotations.filter(_._1.POS.contentEquals("MD")).filter(_._1.word.contains("ould"))
    val modalExpressions = modals.map { m =>
      val modalIdx = m._2
      val start = findPosIndex(annotations,"PRP",modalIdx,5,false)
      val end = findPosIndex(annotations,"VB",modalIdx,3,true)
      if (start!= -1 && end != -1) {
        val expression = annotations.filter((start to end) contains _._2)
        Some(Expression("CRITIQUE",expression.map(_._1.word).mkString(" "),start,end))
      } else None
    }
    modalExpressions.flatten
  }

  def findPosIndex(annotations:List[(TapAnnotation,Int)],posStr:String,start:Int,max:Int,forward:Boolean=true):Int = {
    val range = if(forward) {
      val end = if((start+max) < annotations.length) start + max else annotations.length -1
      (start to end)
    } else {
      val end = if((start-max) >= 0) start - max else 0
      (end to start)
    }
    val filtered = annotations.filter(range contains _._2).filter { case(a,i) => a.POS.startsWith(posStr) || a.word.contains(".")}
    if (filtered.isEmpty) -1 else if(forward) filtered.head._2 else filtered.reverse.head._2
  }
*/
}
