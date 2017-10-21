package tap.services.corpora.gradebook.data

//import spray.json.{DefaultJsonProtocol, JsonFormat}

/**
  * Created by andrew@andrewresearch.net on 12/05/2016.
  */


case class FileMetadata(uid:String,subjectCode:String,assignmentName:String,submitDateTime:String,userGrade:String,docFile:String)
/*
object FileMetadataJsonProtocol extends DefaultJsonProtocol {
  implicit val FileJsonFormat: JsonFormat[FileMetadata] = jsonFormat6(FileMetadata)
}*/


case class OriginalMetadata(Name:String, Assignment:String, DateSubmitted:String, CurrentGrade:String, SubmissionField:String, Comments:String, Files:List[String])
