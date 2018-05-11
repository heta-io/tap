package tap.services.corpora.gradebook.data

//import spray.json.{DefaultJsonProtocol, JsonFormat}

/**
  * Created by andrew@andrewresearch.net on 12/05/2016.
  */



case class ArchiveMetadata(subjectCode:String,assignmentName:String,
                           metaDocCount:Int,textDocCount:Int,userDocCount:Int,excludedDocCount:Int,
                           earliestDate:String,latestDate:String)
/*
object ArchiveMetadataJsonProtocol extends DefaultJsonProtocol {
  implicit val ArchiveJsonFormat: JsonFormat[ArchiveMetadata] = jsonFormat8(ArchiveMetadata)
}
*/