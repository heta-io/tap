//package tap.services.feedback
//
///**
//  * Created by andrew@andrewresearch.net on 20/07/2016.
//  */
//object SpellText {
//  val showCounts = false
//  def docVeryFewErrors="Your document appears to have very few words that AWA has not recognised which normally means that your spelling is good. However, you should always proofread your writing before submitting for assessment."
//  def docSomeErrors(spellCount:Int) = "Your document appears to have a number of words that AWA does not recognise. Try using word processing software to locate the specific mistakes, correct your document, and try submitting it again to AWA."
//  def docQuiteAlotErrors(spellCount:Int) = "Your document has quite a lot of words that AWA does not recognise. With this many errors, AWA may not accurately detect other features in your writing. Try using word processing software to find the specific errors, correct them, and resubmit your document."
//  def docTooManyErrors(spellCount:Int) = "Your document appears to have quite a lot of spelling errors which will prevent AWA from analysing it properly it properly. Try using word processing software to find the specific errors, correct them, and resubmit your document."
//  def paraNoErrors = "There doesn't appear to be any spelling errors in this paragraph - Well done."
//  def paraSomeErrors = "There are words in this paragraph that AWA does not recognise. Please check your spelling and grammar."
//}
//
//object RhetoricalMovesText {
//  def docMissingMoves = "Your document appears to be lacking one or more of the key sentence level features that AWA expects to find in reflective writing. These are represented as coloured icons located at the beginning of sentences. If you need to check what each icon represents, click on the guidance button on the left."
//  def docFewMoves = "In reflective writing, AWA expects to find more sentence level features than what has been identified in your document. These are represented as coloured icons located at the beginning of sentences. This may be intentional on your part, and it does not necessarily mean that your writing is not good, but you should check this with your assessment rubric or one of your subject tutors as your writing may not be as reflective as it should be."
//  def docImbalance = "Your document does not appear to have a good balance of the key sentence level features that AWA expects to find in reflective writing. These are represented as coloured icons located at the beginning of sentences. In reflective writing, AWA does not expect to have very few of one type of sentence feature together with a large quantity of another. Check this against your assessment rubric or with one of your subject tutors as you may be missing a key element of good reflective writing."
//  def docTooMany = "AWA does not expect to find this many sentence level features in your writing. These are represented as coloured icons located at the beginning of sentences. Ensure that your writing is of an appropriate style for your subject. AWA does not check style so you may need to refer to your assessment rubric or check with a subject tutor."
//  def default = "AWA has identified some sentence level features in your writing. These are represented as coloured icons located at the beginning of sentences. Check your assessment rubric to ensure that your document is structured according to the subject requirements."
//
//}
//
//object ExpressionText {
//  def noExpressions = "In this paragraph, AWA was unable to identify expressions of self-critique, expressions of knowledge/learning, or words that evoke strong feelings."
//  def onlyEmotion = "In this paragraph, AWA was unable to identify expressions of self-critique or knowledge/learning."
//  def default = ""
//
//  def strongAffect = "You appear to have used quite a number of words that evoke strong feelings. An important aspect of reflective writing is stating how you feel about the key challenge/s that you are reflecting upon."
//  def moderateAffect = "This paragraph includes the use of some words that evoke strong feelings (dashed underline). Reflective writing uses this kind of language to express how you feel about a problem or challenge. Have you done this in this paragraph?"
//  def weakAffect = "Good reflective writing requires more than just description. Are you expressing how you feel about the key challenge/s that you are reflecting on? Check your assignment requirements to see if this is something that should be included at this point in your writing."
//}
//
//object DummyText {
//
//  /*
//  private def dummyParagraphFeedback(quantity:Int) = (1 to quantity).map( parNum => AwaOutputData("text","Dummy Paragraph Feedback",List("This is placeholder feedbackQuery for paragraph "+parNum,"It is intended for testing purposes, and to assist with UI development. Actual feedbackQuery may be shorter or longer than this."),parNum,parNum,0,0)).toList
//
//  private def dummyDocumentFeedback = AwaOutputData("text","Dummy Document Feedback",List("This is placeholder feedbackQuery for the document","It is intended for testing and UI development. This type of feedbackQuery will contain comments that apply to the document as a whole."),0,0,0,0)
//  */
//
//}