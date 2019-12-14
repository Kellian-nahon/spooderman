package com.epita.indexer.core

import com.epita.indexer.controller.dto.DocumentResponse
import com.epita.indexer.controller.dto.QueryResponse
import com.epita.indexer.tokenisation.Tokenizer
import com.epita.indexer.vectorisation.Vectorizer
import com.epita.reussaure.bean.LogBean

class DefaultQuerying(private val tokenizer: Tokenizer, private val vectorizer: Vectorizer,
                      private val similarityComputer: SimilarityComputer): Querying, LogBean {

    override fun getDocuments(query: String): QueryResponse {
        logger().info("Get Documents matching query: $query")
        val queryVector = vectorizer.vectorize(tokenizer.tokenize(query))
        return QueryResponse(similarityComputer.getDocsWithSimilarity(queryVector).map { (doc, _) -> DocumentResponse(doc.url, doc.content.take(250))}.toList())
    }

}