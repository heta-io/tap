//package tap.services.analytics.analysers



  /*

  private def analyseWithXip(inputData:InputData):Future[DocumentXip] = {
    for {
      xipJson <- ask(xipClient,inputData).mapTo[String]
      decodedJson <- Future(xmlDecode(xipJson))
      xip <- Future(xipResponsesFromJson(decodedJson))
    } yield xip.getOrElse(DocumentXip(List()))
  }

  private def xipResponsesFromJson(xipJson:String):Option[DocumentXip] = {
    xipFromJson(xipJson) match {
      case Success(data) => Some(DocumentXip(data))
      case Failure(ex) => {
        log.error("Unable to retrieve XIPResponse from json string: "+xipJson.take(100))
        log.debug("Error message: "+ex)
        None
      }
    }
  }

  private def xmlDecode(s:String):String = {
    val decoded = s.replaceAllLiterally("&lt;","<")
      .replaceAllLiterally("&apos;","'")
      .replaceAllLiterally("&gt;",">")
      .replaceAllLiterally("&amp;","&")
    log.debug("decoded text: "+decoded)
    decoded
  }

  implicit val serialformats = Serialization.formats(NoTypeHints)

  private def xipFromJson(jsonStr:String):Try[List[SentenceXip]] = Try {read[List[SentenceXip]](jsonStr)}

*/

