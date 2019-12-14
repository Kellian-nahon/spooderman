package com.epita.documentizer.tokenisation

class StemmingReducer(private val suffixes: Sequence<String>): WordTransformer {
    override fun apply(word: String): String? {
        for (suffix in suffixes) {
            if (word.endsWith(suffix)) {
                return word.removeSuffix(suffix)
            }
        }
        return word
    }
}