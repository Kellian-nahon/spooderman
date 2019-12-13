package com.epita.indexer.core

import kotlin.math.log
import kotlin.math.sqrt


typealias TokenVector = Map<String, Pair<Float, List<Int>>>
typealias VectorTfIdf = Map<String, Double>


class SimilarityComputer(private val retroIndex: RetroIndex) {

    private fun getIdf(nbDocumentInCorpus: Int, nbDocumentContainingTerm: Int): Double {
        return log(nbDocumentInCorpus.toDouble() / (1 + nbDocumentContainingTerm).toDouble(), 10.0)
    }

    private fun getNormalizedVectorTfIdf(tokens: TokenVector): VectorTfIdf {
        return normalize(tokens.map { (word, value) ->
            var tfIdf = getIdf(retroIndex.documentsCount, retroIndex.forWord(word).count()) * value.first
            Pair(word, tfIdf)
        }.toMap())
    }

    private fun getMatchedDocuments(queryVector: TokenVector): Set<Document> {
        var matchedDocuments = HashSet<Document>()
        queryVector.forEach { (word, _) ->
            retroIndex.forWord(word).map { doc ->
                if (!matchedDocuments.contains(doc))
                    matchedDocuments.add(doc)
            }
        }
        return matchedDocuments
    }

    fun getDocsWithSimilarity(queryVector: TokenVector): List<Pair<Document, Double>> {
        val queryVectorTfIdf = getNormalizedVectorTfIdf(queryVector)
        val matchedDocuments = getMatchedDocuments(queryVector)
        val vectorsOfDocuments = computeVectorsOfDocuments(queryVectorTfIdf, matchedDocuments)
        var listOfRank = vectorsOfDocuments.map { (doc, vector) ->
            Pair(doc, computeSimilarity(queryVectorTfIdf, vector))
        }.toList()
        return listOfRank.sortedByDescending { it.second }

    }

    private fun computeSimilarity(queryVectorTfIdf: VectorTfIdf, documentVectorTfIdf: VectorTfIdf): Double {
        return (dotProduct(queryVectorTfIdf, documentVectorTfIdf) / euclideanDist(queryVectorTfIdf, documentVectorTfIdf))
    }

    private fun dotProduct(vect1: VectorTfIdf, vect2: VectorTfIdf): Double {
        var product = 0.0
        // Loop for calculate cot product
        vect1.forEach { (word, tfIdf) ->
            if (vect2.containsKey(word)) {
                val tfIdf2: Double = vect2[word] ?: 1.0
                product += tfIdf * tfIdf2
            }
        }
        return product
    }

    private fun euclideanDist(vect1: VectorTfIdf, vect2: VectorTfIdf): Double {
        var diffSquareSum = 0.0
        vect1.forEach { (word, tfIdf) ->
            if (vect2.containsKey(word)) {
                val tfIdf2: Double = vect2[word] ?: 1.0
                diffSquareSum += (tfIdf - tfIdf2) * (tfIdf - tfIdf2)
            }
        }
        return sqrt(diffSquareSum)
    }

    private fun computeVectorsOfDocuments(queryVectorTfIdf: VectorTfIdf, docs: Set<Document>): Map<Document, VectorTfIdf> {
        return docs.map { doc ->
            Pair(doc, normalize(computeVectorOfDocument(queryVectorTfIdf, doc)))
        }.toMap()
    }

    private fun computeVectorOfDocument(queryVectorTfIdf: VectorTfIdf, doc: Document): VectorTfIdf {
        return queryVectorTfIdf.map {(word, _) ->
            var tfIdf = 0.0
            if (doc.tokens.contains(word)) {
                tfIdf = getIdf(retroIndex.documentsCount, retroIndex.forWord(word).count()) * (doc.vector[word] ?: error("")).first
            }
            Pair(word, tfIdf)
        }.toMap()

    }

    private fun normalize(vectorTfIdf: VectorTfIdf): VectorTfIdf {
        val norm = sqrt(vectorTfIdf.map { (_, tf_idf) ->
            tf_idf*tf_idf
        }.sum())
        if (vectorTfIdf.size == 1)
            return vectorTfIdf.map { (word, _) ->
                Pair(word, norm)
            }.toMap()
        return vectorTfIdf.map { (word, tf_idf) ->
            Pair(word, (tf_idf/norm))
        }.toMap()
    }



}