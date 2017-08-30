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

package au.edu.utscic.tap.pipelines

import akka.NotUsed
import akka.stream.scaladsl.Flow

/*****************************************
  *  Cleaning
  *      The pipelines for cleaning text
  *      Nested object holds pipelines that take a
  *      stream of Char and produce a string stream
  */
object Cleaning  {

  /****************************************
    *  Pipeline
    *      A convenience object that holds the pipelines
    *      for cleaning
    */
  object Pipeline { //Flow[ByteString,String,NotUsed]
    val revealInvisible:Flow[String,String,NotUsed] = utf8Str via visibleWhitespace via replaceControl
    val simplify:Flow[String,String,NotUsed] = utf8Str via simplifyQuotes via simplifyHyphens
    val lengthPreserve:Flow[String,String,NotUsed] = utf8Str via simplifyWhitespace via replaceControl
    val utfMinimal:Flow[String,String,NotUsed] = utf8Str via simplifyWhitespace via stripControl via reduceSpace
    val utfSimplify:Flow[String,String,NotUsed] = utf8Str via simplifyWhitespace via simplifyQuotes via simplifyHyphens via stripControlExtended via reduceSpace
    val asciiOnly:Flow[String,String,NotUsed] = utfSimplify via stripNonAscii

  }

  val utf8Str:Flow[String,String,NotUsed] = Flow[String].map(str => str) //.map(_.utf8String)

  val visibleWhitespace:Flow[String,String,NotUsed] = Flow[String].map { str =>
    str.replaceAll(White.rgx_space,Replace.dot) // Spaces
      .replaceAll(White.rgx_line,Replace.not) // Line endings
  }

  val replaceControl:Flow[String,String,NotUsed] = Flow[String].map { str =>
    str.map {c =>
      if(CharFilter.controlExt(c)) Replace.qmk else c
    }.mkString
  }

  val simplifyWhitespace:Flow[String,String,NotUsed] = Flow[String].map { str =>
    str.replaceAll(White.rgx_space,White.sp)
      .replaceAll(White.rgx_line,White.nl)
  }

  val simplifyQuotes:Flow[String,String,NotUsed] = Flow[String].map { str =>
    str.replaceAll(Quote.rgx_dblCurl,Quote.doubleQuote)
      .replaceAll(Quote.rgx_sglCurl,Quote.singleQuote)
  }

  val simplifyHyphens:Flow[String,String,NotUsed] = Flow[String].map { str =>
    str.replaceAll(Hyphen.rgx_hyphens,Hyphen.ascii)
  }

  val stripControl:Flow[String,String,NotUsed] = Flow[String].map { str =>
    str.filterNot(CharFilter.allControl)
  }

  val stripControlExtended:Flow[String,String,NotUsed] = Flow[String].map { str =>
    str.filterNot(CharFilter.controlExt)
  }

  val stripNonAscii:Flow[String,String,NotUsed] = Flow[String].map { str =>
    str.filterNot(CharFilter.above127)
  }

  val reduceSpace:Flow[String,String,NotUsed] = Flow[String].map { str =>
    str.foldLeft("") { (s:String,c:Char) =>
      if(White.isSpace(c)) { //will end with space
        if(s.endsWith(White.sp) || s.endsWith(White.nl)) { s }
        else { s + White.sp }
      } else if(White.isLineEnd(c)) { //will end with newline
        if(s.endsWith(White.sp)) { s.dropRight(1) + White.nl }
        else if(s.endsWith(White.nl)) { s }
        else { s + c }
      } else { s + c }
    }
  }



  //Character Objects

  object White {
    val tab = "\u0009"
    val nl = "\u000a"
    val cr = "\u000d"
    val sp = "\u0020"
    val nb = "\u00a0"
    val rgx_space = s"$sp|$nb|$tab"
    val rgx_line = s"$nl|$cr"
    val rgx_all = s"$rgx_space|$rgx_line"

    def isSpace(c:Char):Boolean = sp.contains(c) || nb.contains(c) || tab.contains(c)
    def isLineEnd(c:Char):Boolean = nl.contains(c) || cr.contains(c)
  }

  object Replace {
    val not = "\u00ac"
    val dot = "\u00b7"
    val qmk = "\ufffd"
  }

  object Quote {
    val singleCurlyLeft = "\u2018"
    val singleCurlyRight = "\u2019"
    val doubleCurlyLeft = "\u201c"
    val doubleCurlyRight = "\u201d"
    val doubleQuote = """\u0022"""
    val singleQuote = "\u0027"
    val rgx_dblCurl = s"$doubleCurlyLeft|$doubleCurlyRight"
    val rgx_sglCurl = s"$singleCurlyLeft|$singleCurlyRight"
  }

  object Hyphen {
    val ascii = "\u002d"
    val soft = "\u00ad"
    val unicode = "\u2010"
    val nb = "\u2011"
    val fig = "\u2012"
    val en = "\u2013"
    val em = "\u2014"
    val bar = "\u2015"
    val minus = "\u2212"
    val rgx_hyphens = s"$soft|$unicode|$nb|$fig|$en|$em|$bar|$minus"
  }

  object CharFilter {
    val notNewline:(Char) => Boolean = (c:Char) => c != White.nl.head
    val lowerControl:(Char) => Boolean = (c:Char) => (c <= '\u001f') && notNewline(c)
    val middleControl:(Char) => Boolean = (c:Char) => c >= '\u007f' && c <= '\u009f'
    val allControl:(Char) => Boolean = (c:Char) => lowerControl(c) || middleControl(c)
    val extended:(Char) => Boolean = (c:Char) => c >= '\u0100'
    val controlExt:(Char) => Boolean = (c:Char) => extended(c) || allControl(c)
    val above127:(Char) => Boolean = (c:Char) => c > '\u007e'
  }

}
