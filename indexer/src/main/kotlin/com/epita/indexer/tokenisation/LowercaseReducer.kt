package com.epita.indexer.tokenisation

class LowercaseReducer: WordTransformer {
    override fun apply(word: String): String? {
        return word.toLowerCase()
    }
}