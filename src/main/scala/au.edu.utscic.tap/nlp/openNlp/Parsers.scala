package au.edu.utscic.tap.nlp.openNlp

import opennlp.tools.cmdline.parser.ParserTool
import opennlp.tools.lemmatizer.{LemmatizerME, LemmatizerModel}
import opennlp.tools.parser.{Parse, Parser, ParserFactory, ParserModel}
import opennlp.tools.postag.{POSModel, POSTaggerME}
import opennlp.tools.sentdetect.{SentenceDetectorME, SentenceModel}
import opennlp.tools.tokenize.{TokenizerME, TokenizerModel}

/**
  * Created by andrew@andrewresearch.net on 20/5/17.
  */
object Parsers {

    lazy val sentenceDetector:Option[SentenceDetectorME] = ModelLoader.load(classOf[SentenceModel]).map(m => new SentenceDetectorME(m))
    lazy val tokeniser:Option[TokenizerME] = ModelLoader.load(classOf[TokenizerModel]).map(m => new TokenizerME(m))
    lazy val posTagger:Option[POSTaggerME] = ModelLoader.load(classOf[POSModel]).map(m => new POSTaggerME(m))
    //lazy val lemmatizer:Option[LemmatizerME] = ModelLoader.load(classOf[LemmatizerModel]).map(m => new LemmatizerME(m))
    lazy val parser:Option[Parser] = ModelLoader.load(classOf[ParserModel]).map(m => ParserFactory.create(m))

    def sentence(text:String):List[String] = sentenceDetector match {
        case Some(sdr) => sdr.sentDetect(text).toList
        case _  => List()
    }

    def token(text:String):List[String] =  tokeniser match {
        case Some(tkr) => tkr.tokenize(text).toList
        case _ => List()
    }

    def posTag(tokens:List[String]):List[String] = posTagger match {
        case Some(ptr) => ptr.tag(tokens.toArray).toList
        case _ => List()
    }

//    def lemma(tokens:List[String],posTags:List[String]):List[String] = lemmatizer match {
//        case Some(lmr) => lmr.lemmatize(tokens.toArray,posTags.toArray).toList
//        case _ => List()
//    }

    def parseTree(text:String):String = parser match {

        case Some(psr) => {
            val parseTree:Array[Parse] = ParserTool.parseLine(text,psr,1)
            var sb:StringBuffer = new StringBuffer()
            parseTree.head.show(sb)
            sb.toString
        }
        case _ => ""
    }


}
