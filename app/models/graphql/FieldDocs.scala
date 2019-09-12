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

/**
  * Provides the documentation information for the QueriesPage.
  */

object FieldDocs {

  case class FieldDoc(
    name: String,
    description:String,
    notebook: String,
    parameters:Map[String,String],
    exampleQuery:String
  )

  val fields: Map[String, FieldDoc] = Map(

    "affectExpressions" -> FieldDoc(
      "affectExpressions",
      """
        |Affect Expressions is a query that will return stats about the valence, arousal and dominance language used.
        |
        |You are able to pass in the thresholds at which each of them will trigger.
        |
        |params = '''
        |{
        |    "valence":4,
        |    "arousal":4,
        |    "dominance":4
        |}
        |'''
      """.stripMargin,
      "https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Affect%20Expressions.ipynb",
      Map("arousal" -> "The threshold where it will return if the analysed text is over this value",
          "dominance" -> "The threshold where it will return if the analysed text is over this value",
          "valence" -> "The threshold where it will return if the analysed text is over this value"),
      """
        |query AffectExpressions($input: String) {
        |    affectExpressions(text:$input,parameters:"{\"valence\":4,\"arousal\":4,\"dominance\":4}") {
        |        querytime
        |        message
        |        timestamp
        |        analytics {
        |            affect {
        |                text
        |                valence
        |                arousal
        |                dominance
        |                startIdx
        |            }
        |        }
        |    }
        |}
      """.stripMargin
    ),
    "annotations" -> FieldDoc(
      "annotations",
      """
        |Annotation is a query that will splitup the text into json data,
        |including seperating the sentences into their own array and providing various stats on each word.
        |
        |The stats provided for each word:
        |
        |- lemma = provides the intended meaning of the word based on it's inflection You can find out
        |more about Lemmatisation > https://en.wikipedia.org/wiki/Lemmatisation
        |- parent = returns the word this word is dependant on
        |- pos tag = returns the part of speech tag for this word,
        |learn more > https://nlp.stanford.edu/software/tagger.shtml
        |- child = returns the word that is dependant on this word
        |- dep type = returns the dependency type,
        |learn more > https://nlp.stanford.edu/software/dependencies_manual.pdf
        |- ner tag = returns the named entity recognized if any.
        |learn more > https://nlp.stanford.edu/software/CRF-NER.shtml
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
    "batch" -> FieldDoc(
      "batch",
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
        |  batch(parameters:"{\"analysisType\":\"reflectExpressions\",\"s3bucket\":\"myBucket\"}") {
        |    message
        |    timestamp
        |    querytime
        |    analytics
        |  }
        |}
      """.stripMargin
    ),
    "clean" -> FieldDoc(
      "clean",
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

    "expressions" -> FieldDoc(
      "expressions",
      """
        |Expressions ia a query that will extract the epistemic expressions of a sentence and list each sentence in it's own array.
      """.stripMargin,
      "https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Expressions.ipynb",
      Map("none" -> "no parameters required"),
      """
        |query Expressions($input: String) {
        |  expressions(text:$input) {
        |    analytics {
        |      sentIdx
        |      affect{
        |        text
        |      }
        |      epistemic {
        |        text
        |        startIdx
        |        endIdx
        |      }
        |      modal {
        |        text
        |      }
        |    }
        |  }
        |}
      """.stripMargin
    ),

    "metrics" -> FieldDoc(
      "metrics",
      """
        |Metrics is a query that will return various stats on the text that was parsed. Metrics such as:
        |
        |- word count
        |- sentence count
        |- average word counts
        |- array of sentences and word counts per sentence
      """.stripMargin,
      "https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Metrics.ipynb",
      Map("none" -> "no parameters required"),
      """
        |query Metrics($input: String) {
        |    metrics(text:$input) {
        |        analytics {
        |            words
        |            sentences
        |            sentWordCounts
        |            averageSentWordCount
        |        }
        |        querytime
        |        message
        |        timestamp
        |    }
        |}
      """.stripMargin
    ),

    "posStats" -> FieldDoc(
      "posStats",
      """
        |Part of speech stats is a query that will return the verb, noun and adjective distribution ratios of the sentences.
      """.stripMargin,
      "https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/PosStats.ipynb",
      Map("none" -> "no parameters required"),
      """
        |query PosStats($input: String){
        |  posStats(text:$input) {
        |    analytics {
        |      verbNounRatio
        |      futurePastRatio
        |      adjectiveWordRatio
        |      namedEntityWordRatio
        |      nounDistribution
        |      verbDistribution
        |      adjectiveDistribution
        |    }
        |  }
        |}
      """.stripMargin
    ),

    "reflectExpressions" -> FieldDoc(
      "reflectExpressions",
      """
        |Reflect Expressions is a query that will return various stats about the text such as:
        |
        |- word counts
        |- average word length
        |- sentence counts
        |- average sentence lengths
        |- meta tags used such as knowledge, experience or regulation
        |- phrase tags used such as outcome, temporal, pertains, consider, anticipate ..etc
      """.stripMargin,
      "https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Reflect%20Expressions.ipynb",
      Map("none" -> "no parameters required"),
      """
        |query ReflectExpressions($input: String) {
        |  reflectExpressions(text:$input) {
        |    querytime
        |    analytics {
        |      counts {
        |        wordCount
        |        avgWordLength
        |        sentenceCount
        |        avgSentenceLength
        |      }
        |      summary {
        |        metaTagSummary {
        |          knowledge
        |          experience
        |          regulation
        |          none
        |        }
        |        phraseTagSummary {
        |          outcome
        |          temporal
        |          pertains
        |          consider
        |          anticipate
        |          definite
        |          possible
        |          selfReflexive
        |          emotive
        |          selfPossessive
        |          compare
        |          manner
        |          none
        |        }
        |      }
        |      tags {
        |        sentence
        |        phrases
        |        subTags
        |        metaTags
        |      }
        |    }
        |  }
        |}
      """.stripMargin
    ),

    "spelling" -> FieldDoc(
      "spelling",
      """
        |Spelling is a query that will return the spelling mistakes and possible suggestions for what the intended word was.
      """.stripMargin,
      "https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Spelling.ipynb",
      Map("none" -> "no parameters required"),
      """
        |query Spelling($input: String) {
        |  spelling(text:$input) {
        |    timestamp
        |    message
        |    querytime
        |    analytics {
        |      sentIdx
        |      spelling {
        |        message
        |        suggestions
        |        start
        |        end
        |      }
        |    }
        |  }
        |}
      """.stripMargin
    ),

    "syllables" -> FieldDoc(
      "syllables",
      """
        |Syllables is a query that will return the syllable count for each word in a sentence and group each sentence into it's own array.
      """.stripMargin,
      "https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Syllables.ipynb",
      Map("none" -> "no parameters required"),
      """
        |query Syllables($input: String) {
        |  syllables(text:$input) {
        |    analytics {
        |      sentIdx
        |      avgSyllables
        |      counts
        |    }
        |    timestamp
        |  }
        |}
      """.stripMargin
    ),

    "vocabulary" -> FieldDoc(
      "vocabulary",
      """
        |Vocabulary is a query that returns the stats on the vocabulary used, It groups them by unique words and how many times they were used.
      """.stripMargin,
      "https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Vocabulary.ipynb",
      Map("none" -> "no parameters required"),
      """
        |query Vocab($input: String) {
        |  vocabulary(text:$input){
        |    analytics {
        |      unique
        |      terms {
        |        term
        |        count
        |      }
        |    }
        |    timestamp
        |  }
        |}
      """.stripMargin
    )

  )
}
