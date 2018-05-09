//package tap.services.analytics.analysers

/**
  * Created by andrew@andrewresearch.net on 9/1/17.
  */
//class TextshapeAnalyser extends Analyser {

//  import context._
//
//  override def analyse(text:String):Future[TextShape] = {
//
//    val chunks = TfIdfShape.chunk(text)
//    val cs = chunks.size
//    val densities:List[Double] = TfIdfShape.density(chunks,TfIdf.calculateWeighted)
//    val ds = densities.size
//    Future(TextShape(cs,ds,densities))
//  }

//}

//case class TextShape(numChunks:Int,numDensities:Int,densities:List[Double])