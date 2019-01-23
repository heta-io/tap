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

import java.io.Serializable

import sangria.macros.derive.{Interfaces, deriveObjectType}
import sangria.schema.{Argument, Context, Field, IntType, InterfaceType, ObjectType, OptionInputType, StringType, fields}
import io.heta.tap.data._
import io.heta.tap.data.doc._
import io.heta.tap.data.doc.expression.{EpistemicExpression, Expressions, ModalExpression}
import io.heta.tap.data.doc.expression.affect.{AffectExpression, AffectExpressions}
import io.heta.tap.data.doc.expression.reflect._
import io.heta.tap.data.doc.spell.{Spell, Spelling}
import io.heta.tap.data.doc.vocabulary.{TermCount, Vocabulary}
import io.heta.tap.data.results._

import scala.concurrent.Future

object Fields {




    object CleanField {
        import Fields.FieldTypes._
        val name ="clean"
        val description = Some(FieldDocs.fields(name).description)
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,StringResult](Interfaces[Unit,StringResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.clean(actions.argOpt(TEXT), actions.argOpt(PARAMETERS))
    }



    object AnnotationsField {
        import Fields.FieldTypes._
        val name ="annotations"
        val description = Some(
          """
            |Annotation is a query that will splitup the text into json data, including seperating the sentences into their own array and providing various stats on each word.
            |
            |The stats provided for each word:
            |
            |- lemma = provides the intended meaning of the word based on it's inflection You can find out more about Lemmatisation [here](https://en.wikipedia.org/wiki/Lemmatisation)
            |- parent = returns the word this word is dependant on
            |- pos tag = returns the part of speech tag for this word, learn more [here](https://nlp.stanford.edu/software/tagger.shtml)
            |- child = returns the word that is dependant on this word
            |- dep type = returns the dependency type, learn more [here](https://nlp.stanford.edu/software/dependencies_manual.pdf)
            |- ner tag = returns the named entity recognized if any. learn more [here](https://nlp.stanford.edu/software/CRF-NER.shtml)
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
            |See [github](https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Annotations.ipynb) for examples and descriptions.
          """.stripMargin)
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,SentencesResult](Interfaces[Unit,SentencesResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.annotations(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }



    object VocabularyField {
        import Fields.FieldTypes._
        val name ="vocabulary"
        val description = Some(
          """
            |Vocabulary is a query that returns the stats on the vocabulary used, It groups them by unique words and how many times they were used.
            |
            |See [github](https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Vocabulary.ipynb) for examples and descriptions.
          """.stripMargin)
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,VocabularyResult](Interfaces[Unit,VocabularyResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.vocabulary(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }



    object MetricsField {
        import Fields.FieldTypes._
        val name ="metrics"
        val description = Some(
          """
            |Metrics is a query that will return various stats on the text that was parsed. Metrics such as:
            |
            |- word count
            |- sentence count
            |- average word counts
            |- array of sentences and word counts per sentence
            |
            |See [github](https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Metrics.ipynb) for examples and descriptions.
          """.stripMargin)
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,MetricsResult](Interfaces[Unit,MetricsResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.metrics(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }



    object PosStatsField {
        import Fields.FieldTypes._
        val name ="posStats"
        val description = Some(
          """
            |Part of speech stats is a query that will return the verb, noun and adjective distribution ratios of the sentences.
            |
            |See [github](https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/PosStats.ipynb) for examples and descriptions.
          """.stripMargin)
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,PosStatsResult](Interfaces[Unit,PosStatsResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.posStats(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }



    object SyllablesField {
        import Fields.FieldTypes._
        val name ="syllables"
        val description = Some(
          """
            |Syllables is a query that will return the syllable count for each word in a sentence and group each sentence into it's own array.
            |
            |See [github](https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Syllables.ipynb) for examples and descriptions.
          """.stripMargin)
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,SyllablesResult](Interfaces[Unit,SyllablesResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.syllables(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }



    object SpellingField {
        import Fields.FieldTypes._
        val name ="spelling"
        val description = Some(
          """
            |Spelling is a query that will return the spelling mistakes and possible suggestions for what the intended word was.
            |
            |See [github](https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Spelling.ipynb) for examples and descriptions.
          """.stripMargin)
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,SpellingResult](Interfaces[Unit,SpellingResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.spelling(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }



    object ExpressionsField {
        import Fields.FieldTypes._
        val name ="expressions"
        val description = Some(
          """
            |Expressions ia a query that will extract the epistemic expressions of a sentence and list each sentence in it's own array.
            |
            |See [github](https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Expressions.ipynb) for examples and descriptions.
          """.stripMargin)
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,ExpressionsResult](Interfaces[Unit,ExpressionsResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.expressions(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }



    object ReflectExpressionsField {
        import Fields.FieldTypes._
        val name ="reflectExpressions"
        val description = Some(
          """
            |Reflect Expressions is a query that will return various stats about the text such as:
            |
            |- word counts
            |- average word length
            |- sentence counts
            |- average sentence lengths
            |- meta tags used such as knowledge, experience or regulation
            |- phrase tags used such as outcome, temporal, pertains, consider, anticipate ..etc
            |
            |See [github](https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Reflect%20Expressions.ipynb) for examples and descriptions.
          """.stripMargin)
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,ReflectExpressionsResult](Interfaces[Unit,ReflectExpressionsResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.reflectExpressions(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }



    object AffectExpressionsField {
        import Fields.FieldTypes._
        val name = "affectExpressions"
        val description = Some(
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
            |
            |See [github](https://github.com/infosci-qut/tapclipy/blob/master/NoteBooks/Queries/Affect%20Expressions.ipynb) for examples and descriptions.
          """.stripMargin)
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,AffectExpressionsResult](Interfaces[Unit, AffectExpressionsResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.affectExpressions(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }


    object RhetoricalMovesField {
        import Fields.FieldTypes._
        val name ="moves"
        val description = Some("Returns a list of moves for the input text")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,StringListResult](Interfaces[Unit,StringListResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.moves(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }


    object BatchField {
        import Fields.FieldTypes._
        val name = "batch"
        val description = Some("")
        val arguments = parameters :: Nil
        val deriveType = deriveObjectType[Unit,BatchResult](Interfaces[Unit,BatchResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.batch(actions.argOpt(PARAMETERS))
    }


    object FieldTypes {

        val TEXT = "text"
        val PARAMETERS = "parameters"

        val inputText:Argument[Option[String]] = Argument(TEXT, OptionInputType(StringType))
        val parameters:Argument[Option[String]] = Argument(PARAMETERS,OptionInputType(StringType))

        implicit val ResultType:InterfaceType[Unit,Result] = InterfaceType(
            "Result", fields[Unit, Result](
                Field("timestamp", StringType, resolve = _.value.timestamp),
                Field("querytime", IntType, resolve = _.value.querytime),
                Field("message", StringType, resolve = _.value.message)
            )
        )

        implicit val TokenType:ObjectType[Unit,Token] = deriveObjectType[Unit,Token]()
        implicit val SentenceType:ObjectType[Unit,Sentence] = deriveObjectType[Unit,Sentence]()
        implicit val TermCountType:ObjectType[Unit,TermCount] = deriveObjectType[Unit,TermCount]()
        implicit val VocabType:ObjectType[Unit,Vocabulary] = deriveObjectType[Unit,Vocabulary]()
        implicit val MetricsType:ObjectType[Unit,Metrics] = deriveObjectType[Unit,Metrics]()
        //implicit val TapExpressionType:ObjectType[Unit,Expression] = deriveObjectType[Unit,Expression]()
        implicit val AffectExpressionType:ObjectType[Unit,AffectExpression] = deriveObjectType[Unit,AffectExpression]()
        implicit val ModalExpressionType:ObjectType[Unit,ModalExpression] = deriveObjectType[Unit,ModalExpression]()
        implicit val EpistemicExpressionType:ObjectType[Unit,EpistemicExpression] = deriveObjectType[Unit,EpistemicExpression]()
        implicit val TapExpressionsType:ObjectType[Unit,Expressions] = deriveObjectType[Unit,Expressions]()
        implicit val tapSyllablesType:ObjectType[Unit,Syllables] = deriveObjectType[Unit,Syllables]()
        implicit val TapSpellingType:ObjectType[Unit,Spelling] = deriveObjectType[Unit,Spelling]()
        implicit val TapSpellType:ObjectType[Unit,Spell] = deriveObjectType[Unit,Spell]()
        implicit val TapPosStatsType:ObjectType[Unit,PosStats] = deriveObjectType[Unit,PosStats]()
        implicit val TapMetaTagSummaryType:ObjectType[Unit,MetaTagSummary] = deriveObjectType[Unit,MetaTagSummary]()
        implicit val TapPhraseTagSummaryType:ObjectType[Unit,PhraseTagSummary] = deriveObjectType[Unit,PhraseTagSummary]()
        implicit val TapSummaryType:ObjectType[Unit,Summary] = deriveObjectType[Unit,Summary]()
        implicit val ReflectType:ObjectType[Unit,WordSentenceCounts] = deriveObjectType[Unit,WordSentenceCounts]()
        implicit val CodedType:ObjectType[Unit,SentencePhrasesTags] = deriveObjectType[Unit,SentencePhrasesTags]()
        implicit val TapReflectExpressionsType:ObjectType[Unit,ReflectExpressions] = deriveObjectType[Unit,ReflectExpressions]()
        //implicit val TapAffectExpressionType:ObjectType[Unit,AffectExpression] = deriveObjectType[Unit,AffectExpression]()
        implicit val TapAffectExpressionsType:ObjectType[Unit,AffectExpressions] = deriveObjectType[Unit,AffectExpressions]()
    }

}

