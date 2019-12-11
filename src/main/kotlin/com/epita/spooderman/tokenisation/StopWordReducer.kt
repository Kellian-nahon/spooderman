package com.epita.spooderman.tokenisation

class StopWordReducer(private val stopWords: Set<String>): WordTransformer {
    override fun apply(word: String): String? {
        return if (stopWords.contains(word)) null else word
    }
}