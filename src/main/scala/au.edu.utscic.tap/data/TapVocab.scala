package au.edu.utscic.tap.data

/**
  * Created by andrew@andrewresearch.net on 6/9/17.
  */

case class TermCount(term:String,count:Int)
case class CountTerms(count:Int,terms:List[String])
case class TapVocab(unique: Int, terms: List[TermCount])