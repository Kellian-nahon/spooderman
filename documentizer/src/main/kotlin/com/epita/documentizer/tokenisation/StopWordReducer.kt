package com.epita.documentizer.tokenisation

class StopWordReducer(private val stopWords: Set<String>): WordTransformer {
    override fun apply(word: String): String? {
        return if (stopWords.contains(word)) null else word
    }
}