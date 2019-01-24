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

package models.graphql

object FieldDocs {

  case class FieldDoc(
    description:String,
    notebook: String,
    parameters:Map[String,String],
    exampleQuery:String
  )

  val fields: Map[String, FieldDoc] = Map(
    "annotations" -> FieldDoc(
      """
        |Annotation is a query that will splitup the text into json data,
        |including seperating the sentences into their own array and providing various stats on each word.
        |
        |The stats provided for each word:
        |
        |- lemma = provides the intended meaning of the word based on it's inflection You can find out
        |more about Lemmatisation [here](https://en.wikipedia.org/wiki/Lemmatisation)
        |- parent = returns the word this word is dependant on
        |- pos tag = returns the part of speech tag for this word,
        |learn more [here](https://nlp.stanford.edu/software/tagger.shtml)
        |- child = returns the word that is dependant on this word
        |- dep type = returns the dependency type,
        |learn more [here](https://nlp.stanford.edu/software/dependencies_manual.pdf)
        |- ner tag = returns the named entity recognized if any.
        |learn more [here](https://nlp.stanford.edu/software/CRF-NER.shtml)
        |
        |This query can provide different outcomes based on the pipeline type passed.
        |
        |possible pipelines:
        |
        |- clu = returns the lemma, pos tag and ner tag
        |- standard = returns the lemma, pos tag, parent, children and dep type
        |- fast = returns the lemma and pos tag
        |- ner = returns the lemma, pos tag, parent, children, dep type and ner tag.
        |
      """.stripMargin,
      "https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Annotations.ipynb",
      Map("pipeType"->"Determines which pipeline you wish to use, Pass in one of the above types."),
      """
        |query Annotations($input: String) {
        |    annotations(text:$input,parameters:"{ \"pipeType\":\"clu\" }") {
        |        analytics {
        |          idx
        |          start
        |          end
        |          length
        |          tokens {
        |            idx
        |            term
        |            lemma
        |            postag
        |            parent
        |            children
        |            deptype
        |            nertag
        |          }
        |        }
        |        querytime
        |        message
        |        timestamp
        |    }
        |}
      """.stripMargin
    ),
    "clean" -> FieldDoc(
      """
        |Clean is a query that will clean and format the text depending on which parameters you pass.
        |There are 5 current parameters you can pass.
        |
        |- visible = Replaces all white spaces with dots and new lines with line feeds.
        |- minimal = Removes all extra white spaces and extra new lines, leaving only one of each.
        |- simple = Removes all extra white spaces and extra new lines, leaving only one of each.
        |It will also replace hypens and quotes with their ascii safe equivalents.
        |- preserve = This will replace spaces with dots and preserve the length of the text.
        |- ascii = This will replace all non ascii characters eg any char above 127
        |
      """.stripMargin,
      "https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Clean.ipynb",
      Map("cleanType"->"Determines which type of clean you wish to perform, Pass in one of the above types."),
      """
        |query Clean($input: String) {
        |        clean(text:$input,parameters:"{\"cleanType\":\"visible\" }") {
        |          analytics
        |          querytime
        |          message
        |          timestamp
        |        }
        |      }
      """.stripMargin
    ),
    "batch" -> FieldDoc(
      """
        |Use specified pipelines to analyse a batch of files from a given bucket name and save the analytics to a
        |subdirectory of the source directory.
      """.stripMargin,
      "https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Batch.ipynb",
      Map("analysisType"->"The name of an existing TAP analysis pipeline (e.g. clean, posStats, reflectExpressions",
          "s3bucket" -> "The name of an AWS S3 bucket that has permissions set for TAP to access"
      ),
      """
        |query Batch {
        |  batch(parameters:"{\"analysisType\":\"reflectExpressions\",
        |  \"s3bucket\":\"myBucket\"}") {
        |    message
        |    timestamp
        |    querytime
        |    analytics
        |  }
        |}
      """.stripMargin
    )
  )



}
