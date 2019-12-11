package com.epita.spooderman.tokenisation

class Tokenizer {

    private var synonymHashMap: HashMap<String, String>? = null
    private val stopWordsList = listOf("i", "me", "my", "myself", "we", "our", "ours", "ourselves",
        "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her",
        "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what",
        "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been",
        "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if",
        "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into",
        "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off",
        "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any",
        "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
        "too", "very", "s", "t", "can", "will", "just", "don", "should", "now")


    init {
        synonymHashMap = HashMap()
        this.javaClass.getResourceAsStream("/synonyms_en.txt").bufferedReader().forEachLine {line ->
            val wordList: List<String> = line.split(", ")
                .map { word -> word.removeSuffix(" ")
                    word.removePrefix(" ")}
            wordList.forEach {word ->
                if (wordList.count() != 0 && !synonymHashMap!!.contains(word)) {
                    synonymHashMap!![word] = wordList[0]
                }
            }
        }
    }

    public fun getSynonymHashMap(): HashMap<String, String>? {
        return synonymHashMap
    }

    public fun getTokens(text: String) : List<String> {
        var doc: List<String> = text.split(' ', ',', ';', '!', '?', '.', '\'', '/', '\"')
        return doc
    }

    public fun removeStopWord(words: List<String>): List<String> {
        return words.filter { !stopWordsList.contains(it.toLowerCase()) }
    }

    public fun applyStemming(words: List<String>): List<String> {
        return words.map { stemming(it) }
    }

    private fun stemming(word: String): String{
        word.removeSuffix("s")
        word.removeSuffix("ing")
        return word
    }

    private fun changeToSynonyme(word: String): String {
        synonymHashMap!![word]?.let {
            if (word == stemming(it)) {
                return stemming(it)
            }
        }
        return word
    }

    public fun synonymousReplacement(words: List<String>): List<String> {
        return words.map { changeToSynonyme(it) }
    }

    public fun tokenisation(text: String): List<String> {
        var doc = text.split(' ', ',', ';', '!', '?', '.', '\'', '/', '\"')
            .filter { !stopWordsList.contains(it.toLowerCase()) }
            .map { stemming(it) }
            .map { changeToSynonyme(it) }
        return doc
    }

}