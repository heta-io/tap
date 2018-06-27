/*
 * Copyright (c) 2016-2018 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 */

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