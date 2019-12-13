package com.epita.indexer.controller

import com.epita.indexer.core.Document
import com.epita.indexer.core.RetroIndex
import com.epita.indexer.core.SimilarityComputer
import com.epita.indexer.tokenisation.Tokenizer
import com.epita.indexer.vectorisation.Vectorizer


class ComputeSimilarityController(private val tokenizer: Tokenizer, private val vectorizer: Vectorizer,
                                  private val similarityComputer: SimilarityComputer, private val retroIndex: RetroIndex) {


    fun getDocuments(query: String): List<Document>{
        val queryVector = vectorizer.vectorize(tokenizer.tokenize(query))
        return similarityComputer.getDocsWithSimilarity(queryVector).map { (doc, _) -> doc }.toList()
    }



}