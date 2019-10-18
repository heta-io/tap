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

import java.time.LocalDateTime

/**
  * Created by andrew@andrewresearch.net on 13/05/2016.
  */
/** properties of the file */
trait FileNameProperties

/** properties of the archive file */
case class ArchiveFileNameProperties(name:String,subjectCode:String,assignmentName:String,downloadDateTime:LocalDateTime) extends FileNameProperties

/*
val splitName = fileName.substring(10).split("_").toList

      // Properties from zip file name
      val subjectCode = splitName(0)
      val assignmentName = splitName(1).replaceAll("(?<=\\w)(20)(?=\\w)"," ")
      val downloadDateTime = LocalDateTime
        .parse(splitName(2).replace(".zip",""),DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
        //.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) //datetime downloaded
 */

/** properties of the user file */
case class UserFileNameProperties(name:String,personId:String,assignmentName:String,submitDateTime:LocalDateTime) extends FileNameProperties

/*
val metaSplitName = metaFileName.split("_").toList
        val personId = metaSplitName(1)
        val studAssignmentName =  metaSplitName(0)
        val submitDateTime = LocalDateTime
            .parse(metaSplitName(3).replace(".txt",""),DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
        val submitTime = submitDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
 */