package io.heta.tap.shared


object GraphQLQueries {

  lazy val allQueries:String = affectExpressions

  private val affectExpressions =
    """
      |# TAP Example Queries
      |
      |query Affect($text:String,$parameters:String) {
      |  affectExpressions(text:$text,parameters:$parameters) {
      |    message
      |    timestamp
      |    querytime
      |    analytics {
      |      affect {
      |        text
      |        valence
      |        arousal
      |        dominance
      |        startIdx
      |        endIdx
      |      }
      |    }
      |  }
      |}
      |
      |
    """.stripMargin

  /*

  var instructions = "# TAP GraphiQL Interface\n" +
    "#\n" +
    "# GraphiQL is an in-browser IDE for writing, validating, and\n" +
    "# testing GraphQL queries.\n" +
    //"#\n" +
    "# Type queries into this side of the screen, and you will\n" +
    "# see intelligent typeaheads aware of the current GraphQL type schema and\n" +
    "# live syntax and validation errors highlighted within the text.\n" +
    //"#\n" +
    "# To bring up the auto-complete at any point, just press Ctrl-Space.\n" +
    //"#\n" +
    "# Press the run button above, or Cmd-Enter to execute the query, and the result\n" +
    "# will appear in the pane to the right.\n\n" +
    //"\n" +
    //"# Text Analytics Pipeline\n" +
    "\n"

  var exampleQueries = "# TAP Example Queries\n" +
    "# -------------------\n" +
    "# These queries are just examples of what TAP can do, the\n" +
    "# actual capability of the server at any point in time can\n" +
    "# be found in the Schema - see the Documentation Explorer\n" +
    "# on the right hand side. \n" +
    "# See the TAP Documentation https://heta-io.github.io/tap/\n" +
    "\n" +
    "# All queries require submission of text and return\n" +
    "# analytics,timestamp, message, querytime\n" +
    "\n" +
    "# Tokenise with pipetype (default is 'fast'):\n" +
    "# fast - lemmas and postags\n" +
    "# standard - lemmas, postags, parse data\n" +
    "# ner - lemmas, postags, parse data, nertags\n" +
    "\n" +
    "query Tokenise($input: String!) {\n" +
    "  annotations(text:$input) {\n" +
    "    timestamp\n" +
    "    querytime\n" +
    "    analytics {\n" +
    "      idx\n" +
    "      start\n" +
    "      end\n" +
    "      length\n" +
    "      tokens {\n" +
    "        idx\n" +
    "        term\n" +
    "        lemma\n" +
    "        postag\n" +
    "      }\n" +
    "    }\n" +
    "  }\n" +
    "}\n" +
    "\n" +
    "query TokeniseWithNer($input: String!) {\n" +
    "  annotations(text:$input,pipetype:\"ner\") {\n" +
    "    analytics {\n" +
    "      idx\n" +
    "      start\n" +
    "      end\n" +
    "      length\n" +
    "      tokens {\n" +
    "        idx\n" +
    "        term\n" +
    "        lemma\n" +
    "        postag\n" +
    "        parent\n" +
    "        children\n" +
    "        deptype\n" +
    "        nertag\n" +
    "      }\n" +
    "    }\n" +
    "    timestamp\n" +
    "  }\n" +
    "}\n" +
    "\n" +
      "\n" +
      "query TokeniseWithCLU($input: String!) {\n" +
      "  annotations(text:$input,pipetype:\"clu\") {\n" +
      "    analytics {\n" +
      "      idx\n" +
      "      start\n" +
      "      end\n" +
      "      length\n" +
      "      tokens {\n" +
      "        idx\n" +
      "        term\n" +
      "        lemma\n" +
      "        postag\n" +
      "        parent\n" +
      "        children\n" +
      "        deptype\n" +
      "        nertag\n" +
      "      }\n" +
      "    }\n" +
      "    timestamp\n" +
      "  }\n" +
      "}\n" +
      "\n" +
    "# Other examples\n" +
    "query Expressions($input2:String!) {\n" +
    "  expressions(text:$input2) {\n" +
    "    analytics {\n" +
    "      sentIdx\n" +
    "      affect{\n" +
    "        text\n" +
    "      }\n" +
    "      epistemic {\n" +
    "        text\n" +
    "        startIdx\n" +
    "        endIdx\n" +
    "      }\n" +
    "      modal {\n" +
    "        text\n" +
    "      }\n" +
    "    }\n" +
    "  }\n" +
    "}\n" +
      "query ReflectExpressions($input2:String!) {\n" +
      "  reflectExpressions(text:$input2) {\n" +
      "    querytime\n" +
      "    analytics {\n" +
      "      counts {\n" +
      "        wordCount\n" +
      "        avgWordLength\n" +
      "        sentenceCount\n" +
      "        avgSentenceLength\n" +
      "      }\n" +
      "      summary {\n" +
      "        metaTagSummary {\n" +
      "          knowledge\n" +
      "          experience\n" +
      "          regulation\n" +
      "          none\n" +
      "        }\n" +
      "        phraseTagSummary {\n" +
      "          outcome\n" +
      "          temporal\n" +
      "          pertains\n" +
      "          consider\n" +
      "          anticipate\n" +
      "          definite\n" +
      "          possible\n" +
      "          selfReflexive\n" +
      "          emotive\n" +
      "          selfPossessive\n" +
      "          compare\n" +
      "          manner\n" +
      "          none\n" +
      "        }\n" +
      "      }\n" +
      "      tags {\n" +
      "        sentence\n" +
      "        phrases\n" +
      "        subTags\n" +
      "        metaTags\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}\n" +
    "query Vocab($input: String!) {\n" +
    "  vocabulary(text:$input){\n" +
    "    analytics {\n" +
    "      unique\n" +
    "      terms {\n" +
    "        term\n" +
    "        count\n" +
    "      }\n" +
    "    }\n" +
    "    timestamp\n" +
    "  }\n" +
    "}\n" +
    "query Metrics($input: String!) {\n" +
    "  metrics(text:$input) {\n" +
    "    analytics {\n" +
    "      sentences\n" +
    "      tokens\n" +
    "      words\n" +
    "      characters\n" +
    "      punctuation\n" +
    "      whitespace\n" +
    "      sentWordCounts\n" +
    "      averageSentWordCount\n" +
    "      wordLengths\n" +
    "      averageWordLength\n" +
    "      averageSentWordLength\n" +
    "    }\n" +
    "    timestamp\n" +
    "  }\n" +
    "}\n" +
    "query PosStats($input:String!){\n" +
    "  posStats(text:$input) {\n" +
    "    analytics {\n" +
    "      verbNounRatio\n" +
    "      futurePastRatio\n" +
    "      adjectiveWordRatio\n" +
    "      namedEntityWordRatio\n" +
    "      nounDistribution\n" +
    "      verbDistribution\n" +
    "      adjectiveDistribution\n" +
    "    }\n" +
    "  }\n" +
    "}\n" +
    "query Syllables($input:String!) {\n" +
    "  syllables(text:$input) {\n" +
    "    analytics {\n" +
    "      sentIdx\n" +
    "      avgSyllables\n" +
    "      counts\n" +
    "    }\n" +
    "    timestamp\n" +
    "  }\n" +
    "}\n" +
    "query Spelling($input2:String!) {\n" +
    "  spelling(text:$input2) {\n" +
    "    timestamp\n" +
    "    message\n" +
    "    querytime\n" +
    "    analytics {\n" +
    "      sentIdx\n" +
    "      spelling {\n" +
    "        message\n" +
    "        suggestions\n" +
    "        start\n" +
    "        end\n" +
    "      }\n" +
    "    }\n" +
    "  }\n" +
    "}\n" +
    "\n"; //+
    //   "# Some queries like \"moves\" require external resources and may\n" +
    //   "# fail if those resources are not available.\n" +
    //   "\n" +
    //   "query RhetoricalMoves($input: String!) {\n" +
    //   "  moves(text:$input,grammar:\"analytic\") {\n" +
    //   "    analytics\n" +
    //   "    message\n" +
    //   "    timestamp\n" +
    //   "    querytime\n" +
    //   "  }\n" +
    //   "}\n" +
    //   "# However, you only need to ask for what you want e.g.\n" +
    //   "query MinimalMoves($input:String!) {\n" +
    //   "  moves(text:$input) {\n" +
    //   "    analytics\n" +
    //   "  }\n" +
    //   "}\n" +
    //   "\n" +
    // "\n" +
    // "##############################\n" +
    // "# UTILITY QUERIES\n" +
    // "\n" +
    // "query MakeVisible($input: String!) {\n" +
    // "  visible(text:$input) {\n" +
    // "    analytics\n" +
    // "    timestamp\n" +
    // "  }\n" +
    // "}\n" +
    // "query AllCleaning($input: String!) {\n" +
    // "  clean(text:$input) {\n" +
    // "    analytics\n" +
    // "    timestamp\n" +
    // "  }\n" +
    // "  cleanPreserve(text:$input) {\n" +
    // "    analytics\n" +
    // "  }\n" +
    // "  cleanMinimal(text:$input) {\n" +
    // "    analytics\n" +
    // "  }\n" +
    // "  cleanAscii(text:$input) {\n" +
    // "    analytics\n" +
    // "  }\n" +
    // "}" +
    // "\n";

  var exampleVariables = "{\"input\": \"It didn't take any time for Dr. Smith to review the subject outline by logging onto the LMS. However, I walked into class like a blank canvas. I had no idea what this course was about but I was certain it had something to do with responsibility and leaders. I reflected on this and felt decision making was like second nature, yes I over-thought my decisions whether it was personal or professional but I never thought of the act of having to justify my decisions.\"," +
    "\"input2\": \"Althogh I wasn't certin, I did beleive that I was doing the right thing. Next time I will be sure.\"}";

  */

}
