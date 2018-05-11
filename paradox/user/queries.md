#### TAP Queries

1. visible: remove nonstandard characters in the input text.

    - Analytics feature:

            text with standard characters

2. clean: replace quotes and hyphens with single byte versions.
   
       - Analytics feature:
   
               cleaned text

3. cleanPreserve: replace control characters while preserving length.

    - Analytics feature:

            cleaned text
            
4. cleanMinimal: strip control characters, and reduce whitespace.

    - Analytics feature:

            cleaned text
            
5. cleanAscii: returns ascii safe cleaned text.

    - Analytics feature:

            cleaned text

6. annotations: return sentences for text.

    - Analytics features:

            idx: sentence index
            start: index of the start token of the sentence in the paragraph
            end: index of the end token of the sentence in the paragraph
            length: length of the sentence
            tokens: list of tokens
                - idx: index of token in the sentence
                - term: token
                - lemma: the canonical form of the token
                - postag: a part of speech tag to the token
                
7. expressions: return expressions for text.

    - Analytics features:

            sentIdx: sentence index
            affect: list of affect expressions
                - text: affect expression
            epistemic: list of epistemic expressions
                - text: epistemic expression
                - startIdx: index of the starting token in the epistemic expression 
                - endIdx: index of the ending token in the epistemic expression
            modal: list of modal expressions
                - text: modal expression
                
8. syllables: count syllables in words and calculates averages for sentences.

    - Analytics features:

            sentIdx: sentence index
            avgSyllables: average syllables in the sentence
            counts: list of syllables count for each word in the sentence
            
9. spelling: return spelling errors and suggestions for each sentence.

    - Analytics features:

            sentIdx: sentence index
            spelling: list of spelling errors and suggestions
                message: return message
                suggestions: list of suggestions
                start: index of the starting character of the error in the sentence
                end: index of the ending character of the error in the sentence
                
10. vocabulary: return vocabulary for text.

    - Analytics features:

            unique: number of unique vocabolaries
            terms: list of vocabolaries
                term: vocabulary
                count: number of the vocabolary in the text

11. metrics: return metrics for text.

    - Analytics features:

            sentences: number of sentences in the text
            tokens: total number of tokens
            words: total number of words
            characters: total number of characters
            punctuation: total number of punctuations
            whitespace: total number of whitespace
            sentWordCounts: list of word counts of each sentence
            averageSentWordCount: average word count per sentence
            wordLengths: list of word lengths of each sentence
            averageWordLength: average word length in the text
            averageSentWordLength: average word length per sentence
                           
12. posStats: return posStats for text.

    - Analytics features:

            verbNounRatio: number of verb/ number of noun
            futurePastRatio: number of future/ number of past
            adjectiveWordRatio: number of adjective/ number of word
            namedEntityWordRatio: number of named entity/ number of word
            nounDistribution: list of noun distributions of each sentence
            verbDistribution: list of verb distributions of each sentence
            adjectiveDistribution: list of adjective distributions of each sentence
            