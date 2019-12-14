package com.epita.documentizer.tokenisation

class Tokenizer(private val splitter: Splitter, private val transformers: List<WordTransformer>) {
    fun tokenize(text: String): List<String> {

        var result = splitter.apply(text)
        transformers.forEach { transformer ->
            result = result.mapNotNull(transformer::apply).filter(String::isNotBlank)
        }
        return result
    }
}