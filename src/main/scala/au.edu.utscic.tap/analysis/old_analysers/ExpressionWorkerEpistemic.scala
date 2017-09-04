package au.edu.utscic.tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 15/07/2016.
  */

import akka.actor.{Actor, ActorLogging}

class ExpressionWorkerEpistemic extends Actor with ActorLogging{

  override def receive = {
    //case annotations:Annotations => sender ! epistemic(annotations.tapAnnotations,annotations.paraIndex)
    case _ => log.error("ExpressionWorkerEpistemic received an unknown message!")
  }
/*
  def epistemic(annotations:List[(TapAnnotation,Int)],paraIndex:Int):List[Expression] = {
    val verbs = List("think","thought","believe","believed","guess","guessed","suppose","supposed","sure","certain","confident","learnt","learned","imagine","imagined","wonder","wondered","consider","considered","realise","realised","realize","realized","understand","understood","assume","assumed","admit")
    val firstPerson = annotations.filter(a => verbs.contains(a._1.word))
    val epistemicExpressions = firstPerson.map { fp =>
      val idx = fp._2
      val start = findPosIndex(annotations,"PRP",idx,4,false)
      if(start!= -1) {
        val expression = annotations.filter((start to idx) contains _._2)
        Some(Expression("EPISTEMIC",expression.map(_._1.word).mkString(" "),start,idx))
      } else None
    }
    epistemicExpressions.flatten
  }

  def findPosIndex(annotations:List[(TapAnnotation,Int)],posStr:String,start:Int,max:Int,forward:Boolean=true):Int = {
    val range = if(forward) {
      val end = if((start+max) < annotations.length) start + max else annotations.length -1
      (start to end)
    } else {
      val end = if((start-max) >= 0) start - max else 0
      (end to start)
    }
    val filtered = annotations.filter(range contains _._2).filter {
      case(a,i) => a.POS.startsWith(posStr) || a.word.contains(".")
    }
    val position = if (filtered.isEmpty) -1 else if(forward) filtered.head._2 else filtered.reverse.head._2
    //Avoid starting or ending on full stops
    if (annotations.isDefinedAt(position) && annotations(position)._1.word.contains(".")) {
      if(forward) position -1
      else position +1
    } else position
  }
*/

}
