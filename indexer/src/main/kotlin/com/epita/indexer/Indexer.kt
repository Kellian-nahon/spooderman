package com.epita.indexer

import com.epita.indexer.tokenisation.Tokenizer
import com.epita.indexer.vectorisation.Vectorizer
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.types.Document
import java.net.URL


class Indexer(private val tokenizer: Tokenizer,
              private val vectorizer: Vectorizer) :LogBean {

    fun documentize(documentContent: String, url: URL): Document {
        logger().info("Documentize documentContent associate to url: $url")
        val tokens = tokenizer.tokenize(documentContent)
        val tokensVector = vectorizer.vectorize(tokens)
        return Document(url, documentContent, tokens, tokensVector)
    }

}