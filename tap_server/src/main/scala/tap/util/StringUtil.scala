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

package tap.util

/**
  * Created by andrew@andrewresearch.net on 19/5/17.
  */
object StringUtil {
  def shorten(text:String,num:Int=30) = text.replace("\n"," ").take(num).concat("\u2026")
  def sentenceSplit(text:String):List[String] = text.split("(?<=[.!?])\\s+(?=[A-Z,a-z,0-9,\\$])").toList
  def wordSplit(text:String):List[String] = text.split("\\W+").toList
}
