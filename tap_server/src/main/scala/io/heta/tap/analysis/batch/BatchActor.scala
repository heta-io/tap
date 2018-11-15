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

package io.heta.tap.analysis.batch

import java.util.UUID

import akka.actor.Actor
import akka.stream.alpakka.s3.scaladsl.{MultipartUploadResult, S3Client}
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString
import akka.{Done, NotUsed}
import com.typesafe.scalalogging.Logger
import io.heta.tap.analysis.batch.BatchActor.{AnalyseSource, CheckProgress, INIT, ResultMessage}
import io.heta.tap.pipelines.DocumentAnnotating
import io.heta.tap.pipelines.materialize.FilePipeline.{File, FileInfo}
import io.heta.tap.pipelines.materialize.{ByteStringPipeline, FilePipeline}
import io.heta.tap.pipelines.materialize.PipelineContext.executor

import scala.concurrent.Future

object BatchActor {
  object INIT
  sealed trait AnalysisRequest
  case class AnalyseSource(bucket:String,analysisType:String) extends AnalysisRequest
  case class StartAnalysis(bucket:String,batchId:String, analysisFunction:(String,S3Client,String) => Future[Done]) extends AnalysisRequest
  case class CheckProgress(bucket:String,batchId:String) extends AnalysisRequest
  case class ResultMessage(result:String,message:String)
}

class BatchActor extends Actor {

  val logger: Logger = Logger(this.getClass)

  val awsS3 = new AwsS3Client()
  val da = new DocumentAnnotating()

  val parallelism = 5

  def receive: PartialFunction[Any,Unit] = {
    case INIT => sender ! init
    case as: AnalyseSource => sender ! analyse(as.bucket,as.analysisType)
    case cp: CheckProgress => sender ! progress(cp.bucket,cp.batchId)
    case msg:Any => logger.error(s"BatchActor received unkown msg: $msg")
  }

  def init: Boolean = {
    //Any startup code for the batch process
    //TODO Check that can connect to S3
    true //for now just return true to allow process to continue
  }

  def analyse(bucket:String,analysisType:String): Future[ResultMessage] = {
    logger.warn(s"Started batch $analysisType for bucket: $bucket")
    val batchId = UUID.randomUUID().toString
    //Create the output folder with batchId as name

    //Create the __metadata file and write the batchId, analysisType, and start time in the file

    //Start the process of analysis - on complete need to write the end time in metadata file (and number of files processed?)
    /*  Reflection for batchmode with https://github.com/portable-scala/portable-scala-reflect */
    analysisType match {
      case "affectExpressions" => processFiles(bucket,batchId,da.Pipeline.cluDoc)
      case _ => logger.error(s"No such analysisType: $analysisType")
    }
    //Return immediately the batchId
    Future.successful(ResultMessage(batchId,s"Started $analysisType analysis for bucket $bucket"))
  }

  def progress(bucket:String,batchId:String): Future[ResultMessage] = {
    logger.warn(s"Checking batch progress for: $bucket/$batchId")
    Future.successful(ResultMessage("",s"Progress checked for $bucket/$batchId"))
  }

  private def processFiles(bucket:String,batchId:String,pipeline: Flow[File, File, NotUsed]): Future[Future[Done]] = Future {
    logger.info(s"Processing files for $batchId in $bucket")
    val fileSourcer = (fi:FileInfo) => sourceFileFromS3(bucket,fi.name)
    val fileSource = sourceFileInfoFromS3(bucket).flatMapMerge(parallelism, fileSourcer)
    val fileSink = sinkFileToS3(bucket,batchId)
    FilePipeline(fileSource,pipeline,fileSink).run
  }

  private def sourceFileInfoFromS3(bucket:String): Source[FileInfo, NotUsed] = awsS3.getContentsForBucket(bucket)
    .map(c => FileInfo(c.key,c.size,c.lastModified))


  private def sourceFileFromS3(bucket:String,fileName:String): Source[File, NotUsed] = awsS3.sourceFileFromBucket(bucket,fileName)
    .map[File](bs => File(fileName,bs))


  private def sinkFileToS3(bucket:String,batchId:String): Sink[File, Future[Done]] = Sink.foreachAsync[File](parallelism){ f =>
    val fileName = newFileName(f.name,batchId)
    val sink: Sink[ByteString, Future[MultipartUploadResult]] = awsS3.sinkfileToBucket(bucket,fileName)
    Future(ByteStringPipeline(Source.single[ByteString](f.contents),sink).run)
  }


  private def newFileName(current:String,batchId:String): String = current.dropRight(4).concat(s"-$batchId.txt")



}
