// Copyright (C) 2017 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package models

import sangria.schema.{Argument, Field, ListType, ObjectType, OptionType, Schema, StringType, fields}
import sangria.macros.derive.{deriveObjectType,ObjectTypeDescription}

/**
  * Created by andrew@andrewresearch.net on 25/8/17.
  */

object MessageSpecification {

  implicit val MessageType:ObjectType[Unit,Message] = deriveObjectType[Unit,Message](ObjectTypeDescription("A message"))

  class MessageRepo {
    private val _messages = List(
      Message("This is message 1"),
      Message("And this is msg 2")
    )
    def message(subtext:String):Option[Message] = _messages find (_.text.contains(subtext))

    def messages: List[Message] = _messages
  }

  val qStr = Argument("text", StringType)

  val QueryType = ObjectType("Query", fields[MessageRepo,Unit](
    Field("message",OptionType(MessageType),
      description = Some("Returns a message that includes substring `text`."),
      arguments = qStr :: Nil,
      resolve = c => c.ctx.message(c arg qStr)
    ),
    Field("messages", ListType(MessageType),
      description = Some("Returns a list of all messages."),
      resolve = _.ctx.messages
    )
  ))

  val schema = Schema(QueryType)
  val repo = new MessageRepo

}
