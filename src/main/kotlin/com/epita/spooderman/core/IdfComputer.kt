package com.epita.spooderman.core

import kotlin.math.ln

typealias TokenVector = Map<String, Pair<Float, List<Int>>>


class IdfComputer(val nbDocumentInCorpus: Int) {

    fun getIdf(nbDocumentInCorpus: Int, nbDocumentContainingTerm: Int): Double {
        return ln(nbDocumentInCorpus.toDouble() / (1 + nbDocumentContainingTerm).toDouble())
    }

    fun getVectorTfIdf(retroIndex: RetroIndex, tokens: TokenVector): List<Double> {
        return tokens.map { (word, value) ->
            var nbDocumentContainingToken = retroIndex.forWord(word).count()
            getIdf(nbDocumentInCorpus, nbDocumentContainingToken) * value.first
        }.toList()
    }

}