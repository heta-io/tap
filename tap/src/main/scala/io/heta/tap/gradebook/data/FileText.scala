package tap.services.corpora.gradebook.data

//import spray.json.{DefaultJsonProtocol, JsonFormat}

/**
  * Created by andrew@andrewresearch.net on 13/05/2016.
  */

case class FileText(metaFile:String,text:String)

/*
object FileTextJsonProtocol extends DefaultJsonProtocol {
  implicit val TextJsonFormat: JsonFormat[FileText] = jsonFormat2(FileText)
}
*/
