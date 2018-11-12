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

package controllers.handlers

import java.util.UUID

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import io.heta.tap.analysis.batch.AwsS3Client
import io.heta.tap.analysis.batch.BatchActor.{AnalyseSource, CheckProgress, ResultMessage}
import javax.inject.{Inject, Named}
import models.graphql.Fields.BatchResult
import play.api.Logger

import scala.concurrent.Future
import scala.concurrent.duration._


/* Possible parameters

analysisType: any valid pipeline query name
s3bucket: any valid s3 bucket name which TAP has been given access to
progressCheck: batchId [UUID]

 */


class BatchAnalysisHandler @Inject()(awsS3Client: AwsS3Client, @Named("batch")batch:ActorRef) extends GenericHandler {

  import io.heta.tap.pipelines.materialize.PipelineContext.executor

  val logger: Logger = Logger(this.getClass)

  def analyse(parameters:Option[String],start:Long):Future[BatchResult] = {
    logger.info(s"Batch analysis with parameters: $parameters")
    process(parameters) match {
      case Right(futResMsg:Future[ResultMessage]) => futResMsg.map( rm => BatchResult(rm.result,rm.message,(System.currentTimeMillis() - start).toInt))
      case Left(error) => Future(BatchResult("",error.getMessage,(System.currentTimeMillis() - start).toInt))
    }
  }

  def process(parameters:Option[String]): Either[Throwable,Future[ResultMessage]] = try {
    val bucket = extractParameter[String]("s3bucket",parameters)
    if(bucket.nonEmpty) {
      val analysis = extractParameter[String]("analysisType",parameters)
      if (analysis.nonEmpty) sendRequest(ANALYSE,bucket.get.toString,analysis.get.toString)
      else {
        val progress = extractParameter[String]("progressCheck",parameters)
        if (progress.nonEmpty) {
          val batchId = progress.getOrElse("").asInstanceOf[String]
          val uuid = UUID.fromString(batchId)
          sendRequest(PROGRESS,bucket.get.toString,uuid.toString)
        }
        else throw new Exception("ERROR: No valid parameters provided")
      }
    } else throw new Exception("ERROR: No S3 bucket provided")
  } catch {
    case error => Left(error)
  }


  def sendRequest(requestType:RequestType,bucket:String,payLoad:String):Either[Throwable,Future[ResultMessage]] = {
    implicit val timeout: Timeout = 5.seconds
    //awsS3Client.instance match {
     // case Some(s3client) => {
        val request = requestType match {
          case ANALYSE => AnalyseSource(bucket,payLoad)
          case PROGRESS => CheckProgress(bucket,payLoad)
        }
        val result = (batch ? request).mapTo[Future[ResultMessage]].flatten
        Right(result)
     // }
     // case None => throw new Exception("No S3 Client Found.")
    //}
  }

  type RequestType = String
  lazy val ANALYSE:RequestType = "analyse"
  lazy val PROGRESS:RequestType = "progress"



}

