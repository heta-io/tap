/*
 * Copyright 2016-2017 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tap.analysis

/**
  * Created by andrew@andrewresearch.net on 21/10/17.
  */
object Syllable {

   def count(word:String): Int = {
    val CLE = "([^aeiouy_]le)"
    val CVCE = "([^aeiou_]{1}[aeiouy]{1}[^aeiouy_]{1,2}e)"
    //val VVN = "([aiouCVLEN]{1,2}[ns])"
    val CVVC = "([^aeiou_][aeiou]{2}[^aeiouy_])"
    val CVC = "([^aeiou_][aeiouy][^aeiou_])"
    val CVV = "([^aeiou_][aeiou][aeiouy])"
    val VC = "([aeiou][^aeiou_])"
    val VR = "([aeiouyr]{1,2})"
    val C = "([^aeiou_])"

    word
      .replaceAll("um","_")
      .replaceAll("([aeo])r","_")
      .replaceAll(CLE,"_")
      .replaceAll(CVCE,"_")
      .replaceAll(CVVC,"_")
      .replaceAll(CVC,"_")
      .replaceAll(CVV,"_")
      .replaceAll(VC,"_")
      .replaceAll(VR,"_")
      .replaceAll(C,"").length
  }

}
