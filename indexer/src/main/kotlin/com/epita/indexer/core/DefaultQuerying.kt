package com.epita.indexer.core

import com.epita.indexer.controller.dto.DocumentResponse
import com.epita.indexer.controller.dto.QueryResponse
import com.epita.indexer.tokenisation.Tokenizer
import com.epita.indexer.vectorisation.Vectorizer

class DefaultQuerying(private val tokenizer: Tokenizer, private val vectorizer: Vectorizer,
                      private val similarityComputer: SimilarityComputer): Querying {

    override fun getDocuments(query: String): QueryResponse {
        val queryVector = vectorizer.vectorize(tokenizer.tokenize(query))
        return QueryResponse(similarityComputer.getDocsWithSimilarity(queryVector).map { (doc, _) -> DocumentResponse(doc.url, doc.content.take(250))}.toList())
    }

}