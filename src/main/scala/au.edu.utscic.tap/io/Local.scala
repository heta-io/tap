package au.edu.utscic.tap.io

import java.io.{File, InputStream}
import java.nio.file.{Path, Paths}

import akka.NotUsed
import akka.stream.IOResult
import akka.util.ByteString

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
  * Created by andrew@andrewresearch.net on 1/3/17.
  */
object Local {

  import akka.stream.scaladsl._

  import au.edu.utscic.tap.TapStreamContext._

  def directorySource(directory: String):Source[Path,NotUsed] = {

    val dir = new File(getClass.getResource(directory).getPath)
    println(dir.toString)
    val files = dir.listFiles.filter(f => f.isFile && f.canRead).toList.map(f => Paths.get(f.getAbsolutePath))
    files.foreach(println(_))
    Source(files)
  }

  case class CorpusFile(name:String,contents:String)

  val fileFlow:Flow[Path,Future[CorpusFile],NotUsed] = Flow[Path].map( path =>fileSource(path).toMat(Sink.head[ByteString])(Keep.right).run().map(contents => CorpusFile(path.getFileName.toString,contents.utf8String)))


  def fileSource(path:Path):Source[ByteString,Future[IOResult]] = FileIO.fromPath(path)

 val pipeline = directorySource("/").via(fileFlow)
}
