package com.epita.victaure.controller

import com.epita.victaure.core.Document
import com.epita.victaure.core.RetroIndex
import com.epita.victaure.core.SimilarityComputer
import com.epita.victaure.tokenisation.Tokenizer
import com.epita.victaure.vectorisation.Vectorizer


class ComputeSimilarityController(private val tokenizer: Tokenizer, private val vectorizer: Vectorizer,
                                  private val similarityComputer: SimilarityComputer, private val retroIndex: RetroIndex) {


    fun getDocuments(query: String): List<Document>{
        val queryVector = vectorizer.vectorize(tokenizer.tokenize(query))
        return similarityComputer.getDocsWithSimilarity(queryVector).map { (doc, _) -> doc }.toList()
    }



}