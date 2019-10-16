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

package models

import models.graphql.Fields._
import models.graphql.GraphqlActions
import sangria.schema.{Field, ObjectType, Schema, fields}


class GraphqlSchema {

  private val tapFields = fields[GraphqlActions,Unit](
    Field(CleanField.name,CleanField.deriveType,CleanField.description,CleanField.arguments,CleanField.resolver),
    Field(AnnotationsField.name,AnnotationsField.deriveType,AnnotationsField.description,AnnotationsField.arguments,AnnotationsField.resolver),
    Field(VocabularyField.name,VocabularyField.deriveType,VocabularyField.description,VocabularyField.arguments,VocabularyField.resolver),
    Field(MetricsField.name,MetricsField.deriveType,MetricsField.description,MetricsField.arguments,MetricsField.resolver),
    Field(PosStatsField.name,PosStatsField.deriveType,PosStatsField.description,PosStatsField.arguments,PosStatsField.resolver),
    Field(SyllablesField.name,SyllablesField.deriveType,SyllablesField.description,SyllablesField.arguments,SyllablesField.resolver),
    Field(SpellingField.name,SpellingField.deriveType,SpellingField.description,SpellingField.arguments,SpellingField.resolver),
    Field(ExpressionsField.name,ExpressionsField.deriveType,ExpressionsField.description,ExpressionsField.arguments,ExpressionsField.resolver),
    Field(ReflectExpressionsField.name,ReflectExpressionsField.deriveType,ReflectExpressionsField.description,ReflectExpressionsField.arguments,ReflectExpressionsField.resolver),
    Field(AffectExpressionsField.name,AffectExpressionsField.deriveType,AffectExpressionsField.description,AffectExpressionsField.arguments,AffectExpressionsField.resolver),
    Field(RhetoricalMovesField.name,RhetoricalMovesField.deriveType,RhetoricalMovesField.description,RhetoricalMovesField.arguments,RhetoricalMovesField.resolver),
    Field(BatchField.name,BatchField.deriveType,BatchField.description,BatchField.arguments,BatchField.resolver)
  )

  def create:Schema[GraphqlActions,Unit] = Schema(ObjectType("Query",tapFields))

}
