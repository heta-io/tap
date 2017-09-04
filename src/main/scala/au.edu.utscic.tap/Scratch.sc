import cc.factorie.app.nlp._
var doc = new Document("Education is the most powerful weapon which you can use to change the world.")
val annotator = DocumentAnnotatorPipeline(pos.OntonotesForwardPosTagger, parse.WSJTransitionBasedParser)
annotator.process(doc)
for (token <- doc.tokens)
  println("%-10s %-5s %-4d %-7s".format(token.string, token.posTag.categoryValue, token.parseParentIndex, token.parseLabel.categoryValue))
println(s"Tokens: ${doc.tokens.size}")

doc.owplString(annotator)
doc.asSection.sentences.foreach { s =>
  val tree = s.parse.toString()
  println(tree)
}