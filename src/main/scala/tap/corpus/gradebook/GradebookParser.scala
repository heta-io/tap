package tap.services.corpora.gradebook

/**
  * Created by andrew@andrewresearch.net on 14/10/16.
  */

import org.slf4j.LoggerFactory

import tap.services.corpora.gradebook.data._

class GradebookParser {

  def logger = LoggerFactory.getLogger(this.getClass)

  /*
  def processCurrentDirectory = {
    Files.newDirectoryStream(Paths.get(path)).asScala
      .filter(p => isGradebookZip(p.getFileName.toString))
      .foreach { p =>
        var metadata = processZipfile(p).getOrElse("no metadata").toString
        logger.debug(metadata)
      }
  }
  */

  /*
  def processZipfile(path: Path): Option[ArchiveMetadata] = {

    // Process each zip file

    val archiveNameProps = getArchiveFileNameProperties(path.getFileName.toString)
    logger.info("Reading: " + archiveNameProps.name)
    val archivePaths = getArchiveFilePaths(path)
    logger.debug("meta: " + archivePaths.metadataFilesCount + " noDoc: " + archivePaths.noDocumentFilesCount + " doc: " + archivePaths.documentFilesCount)
    val savePaths = createSavePaths(archiveNameProps)

    var earliestDate = LocalDateTime.MAX
    var latestDate = LocalDateTime.MIN

    var excludedFilesCount = 0

    // Process each metadata file with corresponding document files
    archivePaths.metadataFiles.foreach { filePath: Path =>

      val fileNameProps = getUserFileNameProperties(filePath.getFileName.toString)
      logger.info("Processing: " + fileNameProps.name)

      //Update time range
      if (fileNameProps.submitDateTime.isBefore(earliestDate)) earliestDate = fileNameProps.submitDateTime
      if (fileNameProps.submitDateTime.isAfter(latestDate)) latestDate = fileNameProps.submitDateTime

      // Get the metadata from the metadata file
      val originalMetadata = getOriginalMetadata(Files.lines(filePath).iterator().asScala.mkString("\n"))
      //logger.debug(originalMetadata.toString)

      if (isEmpty(originalMetadata)) {
        excludedFilesCount += 1
      } else {
        //We want to get the appropriate text and save it to the documents directory
        val textInputStream = originalMetadata.Files.size match {
          case 1 => {
            logger.debug("Looks like the student has attached a file, get the text from it.")
            val fileName = originalMetadata.Files.head
            logger.debug("Read " + fileName + " from: " + savePaths.saveDirPath)
            Files.newInputStream(archivePaths.archivePath.resolve(fileName))
          }
          case 0 => {
            logger.debug("No file attached, check the originalMetadata for text.")
            val submit = if (!originalMetadata.SubmissionField.startsWith("There is no student")) originalMetadata.SubmissionField else ""
            val comments = if (!originalMetadata.Comments.startsWith("There are no student")) originalMetadata.Comments else ""
            val text = submit.concat(comments)
            new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))
          }
          case _ => {
            logger.debug("Maybe more than one file attached...")
            //val files = originalMetadata.Files.filter(s => s.contains(".docx") || s.contains("pdf"))
            //files.map(file => Files.newInputStream(archivePaths.archivePath.resolve(file))).last
            new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)) //Empty input stream

          }
        }

        //Clean the text
        logger.info("Cleaning text...")
        val userOutputStream: ByteArrayOutputStream = TextExtractor(textInputStream).plainOutputStream
        logger.debug("Done")

        // New filenames
        val uid = UUID.randomUUID().toString
        val time = getIsoLocalDateTime(fileNameProps.submitDateTime)
        val metaDataFileName = "meta-" + time + "-" + uid + ".json"
        val userDataFileName = "user-" + time + "-" + uid + ".json"

        // Build file
        val fileText = FileText(
          metaDataFileName,
          userOutputStream.toString.trim
        )

        val textJson = fileText.toJson.prettyPrint
        val fileTextInputStream = new ByteArrayInputStream(textJson.getBytes())

        //Save the document
        logger.info("Saving: " + userDataFileName)
        logger.debug("Saving to: " + savePaths.documentPath.toString)
        Files.copy(fileTextInputStream, savePaths.documentPath.resolve(userDataFileName), StandardCopyOption.REPLACE_EXISTING)
        logger.debug("Saved")

        // Build file metadata
        val meta = FileMetadata(
          uid,
          archiveNameProps.subjectCode,
          archiveNameProps.assignmentName,
          getIsoLocalDateTime(fileNameProps.submitDateTime),
          originalMetadata.CurrentGrade,
          userDataFileName)

        val metaJson = meta.toJson.prettyPrint
        val metaInputStream = new ByteArrayInputStream(metaJson.getBytes())

        // Save the metadata
        logger.info("Saving: " + metaDataFileName)
        logger.debug("Saving to " + savePaths.metadataPath.toString)
        Files.copy(metaInputStream, savePaths.metadataPath.resolve(metaDataFileName), StandardCopyOption.REPLACE_EXISTING)
        logger.debug("Saved")
      } //End excluded files
    } // End file iterator

    // Build archive metadata
    val archive = ArchiveMetadata(
      archiveNameProps.subjectCode,
      archiveNameProps.assignmentName,
      archivePaths.metadataFilesCount,
      archivePaths.documentFilesCount,
      archivePaths.noDocumentFilesCount,
      excludedFilesCount,
      earliestDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
      latestDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))

    val archiveJson = archive.toJson.prettyPrint
    val archiveInputSream = new ByteArrayInputStream(archiveJson.getBytes())

    // Save the archive metadata
    val archiveMetaFileName = "archive-metadata-" + path.getFileName.toString + ".json"
    logger.debug("Saving: " + archiveMetaFileName + " to " + savePaths.assignmentPath.toString)
    Files.copy(archiveInputSream, savePaths.assignmentPath.resolve(archiveMetaFileName), StandardCopyOption.REPLACE_EXISTING)
    logger.debug("Saved")

    Some(archive)

  }

  private def isGradebookZip(fileName:String):Boolean = fileName.matches("gradebook_([0-9A-Za-z]*)_(.*)_([0-9\\-]*)\\.zip")

  private def isMetafileName(name:String):Boolean = name.matches("(.*)_([0-9A-Za-z]*)_attempt_([0-9\\-]*)\\.txt")



  private def createSavePaths(archiveProps:ArchiveFileNameProperties):SavePaths = {
    val currentPath = Paths.get(path).toAbsolutePath.getParent
    val subjectPath = currentPath.resolve(archiveProps.subjectCode)
    if(!Files.exists(subjectPath)) Files.createDirectory(subjectPath)
    val assignPath = subjectPath.resolve(archiveProps.assignmentName)
    if(!Files.exists(assignPath)) Files.createDirectory(assignPath)
    val metaPath = assignPath.resolve("metadata")
    if(!Files.exists(metaPath)) Files.createDirectory(metaPath)
    val docPath = assignPath.resolve("documents")
    if(!Files.exists(docPath)) Files.createDirectory(docPath)
    SavePaths(currentPath,subjectPath,assignPath,docPath,metaPath)
  }

  private def getArchiveFileNameProperties(fileName:String):ArchiveFileNameProperties = {
    val splitName = fileName.substring(10).split("_").toList
    ArchiveFileNameProperties(
      fileName, // name
      splitName(0), //subjectCode
      splitName(1).replaceAll("(?<=\\w)(20)(?=\\w)"," "), //assignmentName
      LocalDateTime.parse(splitName(2).replace(".zip",""),DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) //downloadDateTime
    )
  }

  private def getUserFileNameProperties(fileName:String):UserFileNameProperties = {
    val splitName = fileName.split("_").toList
    UserFileNameProperties (
      fileName, // name
      splitName(1), //personId
      splitName(0), //assignmentName
      LocalDateTime.parse(splitName(3).replace(".txt",""),DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) //submitDateTime
    )
  }

  //private def getIsoLocalDateTime(dateTime:LocalDateTime):String =  dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

  private def getOriginalMetadata(text:String):OriginalMetadata = {
    // Properties from within the file
    OriginalMetadata(
      "(?<=Name:\\s)((.*)\\n)*(?=Assignment)".r.findFirstIn(text).getOrElse("").trim, //Name
      "(?<=Assignment:\\s)((.*)\\n)*(?=Date)".r.findFirstIn(text).getOrElse("").trim, //Assignment
      "(?<=Submitted:\\s)((.*)\\n)*(?=Current)".r.findFirstIn(text).getOrElse("").trim, //Date
      "(?<=Grade:\\s)((.*)\\n)*(?=Submission)".r.findFirstIn(text).getOrElse("").trim, //Grade
      "(?<=Submission\\sField:\\s)((.*)\\n)*(?=Comments:)".r.findFirstIn(text).getOrElse("").trim, //SubmissionField
      "(?<=Comments:\\s)((.*)\\n)*(?=Files:)".r.findFirstIn(text).getOrElse("").trim, //Comments
      "(?<=Filename:\\s)(.*)".r.findAllIn(text).toList.map(_.trim) //Files
    )
  }

  private def isEmpty(omd:OriginalMetadata):Boolean = omd.CurrentGrade.contains("Needs Grading") && omd.SubmissionField.startsWith("There is no student") &&
    omd.Files.isEmpty

    */
}
