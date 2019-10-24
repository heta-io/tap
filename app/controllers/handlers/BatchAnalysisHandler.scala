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
import io.heta.tap.analysis.batch.BatchActor.{AnalyseSource, CheckProgress, INIT, ResultMessage}
import io.heta.tap.data.results.BatchResult
import javax.inject.{Inject, Named}
import play.api.Logger

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success


/* Possible parameters
analysisType: any valid pipeline query name
s3bucket: any valid s3 bucket name which TAP has been given access to
progressCheck: batchId [UUID]
 */

/**
  * Handles batch queries where input files are drawn from an S3 bucket rather than sent via the query.
  *
  * @param awsS3Client provides access to amazon-specific S3 features
  * @param batch supports sending messages to the actor it represents in batch
  * @param cluAnnotator annotates sentences with parts of speech, lemmas and dependencies
  */

class BatchAnalysisHandler @Inject()(awsS3Client: AwsS3Client, @Named("batch")batch:ActorRef, @Named("cluAnnotator")cluAnnotator:ActorRef) extends GenericHandler {

  import io.heta.tap.pipelines.materialize.PipelineContext.executor

  val logger: Logger = Logger(this.getClass)

  {
    implicit val timeout: Timeout = 5.seconds
    (batch ? INIT).onComplete{
      case Success(result:Boolean) => if(result) {
        logger.info("BatchActor initialised successfully")
      } else {
        logger.error("There was a problem initialising the BatchActor")
      }
      case scala.util.Failure(exception) => logger.error(exception.getMessage)
    }
  }

  /**
    * Performs the batch analysis with parameters
    *
    * @param parameters Optional version parameters of the object
    * @param start "Long" type
    * @return A [[scala.concurrent.Future Future]] with type [[BatchResult]] which returns Right or Left
    */
  def analyse(parameters:Option[String],start:Long):Future[BatchResult] = {
    logger.info(s"Batch analysis with parameters: $parameters")
    process(parameters) match {
      case Right(futResMsg:Future[ResultMessage]) => futResMsg.map( rm => BatchResult(rm.result,rm.message,(System.currentTimeMillis() - start).toInt))
      case Left(error) => Future(BatchResult("",error.getMessage,(System.currentTimeMillis() - start).toInt))
    }
  }

  /**
    * Handles the process of batchanalysis
    *
    * @param parameters Optional version parameters of the object
    * @return Detects whether a received input is a [[Throwable]] or [[scala.concurrent.Future Future]] of [[ResultMessage]]
    */
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

  /**
    * Operation of sending requests to client
    *
    * @param requestType Request type
    * @param bucket the s3 bucket name
    * @param payLoad
    * @return Detects whether a received input is a [[Throwable]] or [[scala.concurrent.Future Future]] of [[ResultMessage]]
    */
  def sendRequest(requestType:RequestType,bucket:String,payLoad:String):Either[Throwable,Future[ResultMessage]] = {
    implicit val timeout: Timeout = 5.seconds

        val request = requestType match {
          case ANALYSE => AnalyseSource(bucket,payLoad,cluAnnotator)
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