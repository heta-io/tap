package tap.services.corpora.gradebook.data

import java.time.LocalDateTime

/**
  * Created by andrew@andrewresearch.net on 13/05/2016.
  */

trait FileNameProperties

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

case class UserFileNameProperties(name:String,personId:String,assignmentName:String,submitDateTime:LocalDateTime) extends FileNameProperties

/*
val metaSplitName = metaFileName.split("_").toList
        val personId = metaSplitName(1)
        val studAssignmentName =  metaSplitName(0)
        val submitDateTime = LocalDateTime
            .parse(metaSplitName(3).replace(".txt",""),DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
        val submitTime = submitDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
 */