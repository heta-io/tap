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
  * Created by andrew@andrewresearch.net on 12/05/2016.
  */

/** File meta data such as unit id, subject code, assignment name, submit datetime, user grade, and doc file */
case class FileMetadata(uid:String,subjectCode:String,assignmentName:String,submitDateTime:String,userGrade:String,docFile:String)
/*
object FileMetadataJsonProtocol extends DefaultJsonProtocol {
  implicit val FileJsonFormat: JsonFormat[FileMetadata] = jsonFormat6(FileMetadata)
}*/


case class OriginalMetadata(Name:String, Assignment:String, DateSubmitted:String, CurrentGrade:String, SubmissionField:String, Comments:String, Files:List[String])
