package com.epita.victaure.core

import com.epita.spooderman.core.RetroIndex
import kotlin.math.log
import kotlin.math.sqrt


typealias TokenVector = Map<String, Pair<Float, List<Int>>>
typealias VectorTfIdf = Map<String, Double>


class IdfComputer(private val nbDocumentInCorpus: Int) {

    private fun getIdf(nbDocumentContainingTerm: Int): Double {
        return log(nbDocumentInCorpus.toDouble() / (1 + nbDocumentContainingTerm).toDouble(), 10.0)
    }

    fun getVectorTfIdf(retroIndex: RetroIndex, tokens: TokenVector): VectorTfIdf {
        return tokens.map { (word, value) ->
            var nbDocumentContainingToken = retroIndex.forWord(word).count()
            var tfIdf = getIdf(nbDocumentContainingToken) * value.first
            Pair(word, tfIdf)
        }.toMap()
    }

    fun getCosinusSimiliraty(queryVectorTfIdf: VectorTfIdf, vectorsOfDocuments: Map<Document, VectorTfIdf>): List<Pair<Document, Double>> {
        var listOfRank = vectorsOfDocuments.map { (doc, vector) ->
            Pair(doc, computeSimilarity(queryVectorTfIdf, vector))
        }.toList()
        return listOfRank.sortedByDescending { it.second }

    }

    private fun computeSimilarity(queryVectorTfIdf: VectorTfIdf, documentVectorTfIdf: VectorTfIdf): Double {
        return (dotProduct(queryVectorTfIdf, documentVectorTfIdf) / euclidianDist(queryVectorTfIdf, documentVectorTfIdf))
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

    private fun euclidianDist(vect1: VectorTfIdf, vect2: VectorTfIdf): Double {
        var diffSquareSum = 0.0
        vect1.forEach { (word, tfIdf) ->
            if (vect2.containsKey(word)) {
                val tfIdf2: Double = vect2[word] ?: 1.0
                diffSquareSum += (tfIdf - tfIdf2) * (tfIdf - tfIdf2)
            }
        }
        return sqrt(diffSquareSum)
    }

    fun computeVectorsOfDocuments(retroIndex: RetroIndex, queryVectorTfIdf: VectorTfIdf, docs: List<Document>): Map<Document, VectorTfIdf> {
        return docs.map { doc ->
            Pair(doc, normalize(computeVectorOfDocument(retroIndex, queryVectorTfIdf, doc)))
        }.toMap()
    }

    private fun computeVectorOfDocument(retroIndex: RetroIndex, queryVectorTfIdf: VectorTfIdf, doc: Document): VectorTfIdf {
        return queryVectorTfIdf.map {(word, _) ->
            var tfIdf = 0.0
            if (doc.tokens.contains(word)) {
                tfIdf = getIdf(retroIndex.forWord(word).count()) * (doc.vector[word] ?: error("")).first
            }
            Pair(word, tfIdf)
        }.toMap()

    }

    fun normalize(vectorTfIdf: VectorTfIdf): VectorTfIdf {
        val norme = sqrt(vectorTfIdf.map { (_, tf_idf) ->
            tf_idf*tf_idf
        }.sum())
        if (vectorTfIdf.size == 1)
            return vectorTfIdf.map { (word, _) ->
                Pair(word, norme)
            }.toMap()
        return vectorTfIdf.map { (word, tf_idf) ->
            Pair(word, (tf_idf/norme))
        }.toMap()
    }



}