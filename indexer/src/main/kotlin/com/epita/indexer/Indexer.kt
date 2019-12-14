package com.epita.indexer

import com.epita.indexer.tokenisation.Tokenizer
import com.epita.indexer.vectorisation.Vectorizer
import com.epita.spooderman.types.Document


class Indexer(private val tokenizer: Tokenizer,
              private val vectorizer: Vectorizer) {

    fun documentize(documentContent: String): Document {
        val tokens = tokenizer.tokenize(documentContent)
        val tokensVector = vectorizer.vectorize(tokens)
        return Document(documentContent, tokens, tokensVector)
    }

}