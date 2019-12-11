package com.epita.spooderman.tokenisation

class SynonymsReducer(private val synonyms: Map<String, String>): WordTransformer {
    override fun apply(word: String): String? {
        return synonyms.getOrDefault(word, word)
    }
}