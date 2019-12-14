package com.epita.indexer.core

import com.epita.spooderman.types.Document

interface SimilarityComputer {
    fun getDocsWithSimilarity(queryVector: TokenVector): List<Pair<Document, Double>>
}