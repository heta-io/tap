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

import akka.NotUsed
import akka.stream.alpakka.s3.scaladsl.S3
import akka.stream.alpakka.s3._
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString

import io.heta.tap.pipelines.materialize.PipelineContext.materializer

import scala.concurrent.Future

/**
  * Provides access to amazon-specific S3 features
  */

class AwsS3Client { //@Inject() (config:AppConfig) {

  //val config = new AppConfig()

  //lazy val client: S3Client = this.instance.get

  /**
    * Return a source of object metadata for a given bucket with optional prefix using version 2 of the List Bucket API.
    * This will automatically page through all keys with the given parameters.
    *
    * @param bucket the s3 bucket name
    * @param prefix Prefix of the keys you want to list under passed bucket
    * @return A [[akka.stream.scaladsl.Source Source]] of [[ListBucketResultContents]]
    */
  def getContentsForBucket(bucket:String,prefix:Option[String]=None): Source[ListBucketResultContents, NotUsed] = S3.listBucket(bucket,prefix)

  /**
    * Downloads a S3 Object
    * @param bucket the s3 bucket name
    * @param fileName the file name
    * @return A [[akka.stream.scaladsl.Source Source]] which upon materializes will
    *         return a [[scala.concurrent.Future Future]] containing [[ByteString]]
    */
  def sourceFileFromBucket(bucket:String,fileName:String): Source[Future[ByteString], NotUsed] = {
    S3.download(bucket,fileName)
      .map[Future[ByteString]](f => f.getOrElse((Source.empty[ByteString],0))._1.runWith(Sink.head[ByteString]))
  }

  /**
    * Uploads a S3 Object by making multiple requests
    * @param bucket the s3 bucket name
    * @param fileName the file name
    * @return A [[akka.stream.scaladsl.Sink Sink]] that accepts [[ByteString]]'s and materializes to a [[scala.concurrent.Future Future]] of [[MultipartUploadResult]]
    */
  def sinkfileToBucket(bucket:String,fileName:String): Sink[ByteString, Future[MultipartUploadResult]] = {
    S3.multipartUpload(bucket,fileName)
    //S3.multipartUpload(bucket,fileName)
    //  .mapMaterializedValue[Future[MultipartUploadResult]](f => f.runWith(Sink.head[MultipartUploadResult]))
  }

  //private def credentialsProvider(key:String,secret:String) = new AWSStaticCredentialsProvider(new BasicAWSCredentials(key, secret))
//  private def regionProvider(region:String) = new AwsRegionProvider {
//    def getRegion: String = region
//  }

//  private lazy val settings = for {
//    key <- config.getAwsAccessKey
//    secret <- config.getAwsAccessPassword
//    region <- config.getAwsRegion
//  } yield new S3Settings(
//    MemoryBufferType, None,
//    credentialsProvider(key,secret), regionProvider(region),
//    false, None,ListBucketVersion2
//  )
//
//  private def instance: Option[S3Client] = for {
//    s <- settings
//  } yield new S3Client(s)(PipelineContext.system, PipelineContext.materializer)

}
