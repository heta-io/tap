package tap.services.corpora.gradebook.data

import java.nio.file.Path

/**
  * Created by andrew@andrewresearch.net on 13/05/2016.
  */

case class ArchiveFilePaths(archivePath:Path,allFiles:List[Path],metadataFiles:List[Path],documentFiles:List[Path],
                            allFilesCount:Int,metadataFilesCount:Int,documentFilesCount:Int,noDocumentFilesCount:Int)