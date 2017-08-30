package au.edu.utscic.tap.data

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */

case class CorpusTfIdfResults(name:String, selectionCount:Int, selectionSize:Double, weighted:Boolean, results:Map[String,Double])
