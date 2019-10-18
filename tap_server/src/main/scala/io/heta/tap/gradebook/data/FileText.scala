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

package tap.services.corpora.gradebook.data

//import spray.json.{DefaultJsonProtocol, JsonFormat}

/**
  * Created by andrew@andrewresearch.net on 13/05/2016.
  */

/** File text that accepts meta file, and text */
case class FileText(metaFile:String,text:String)

/*
object FileTextJsonProtocol extends DefaultJsonProtocol {
  implicit val TextJsonFormat: JsonFormat[FileText] = jsonFormat2(FileText)
}
*/
