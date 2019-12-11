package com.epita.spooderman.core

import kotlin.math.ln

typealias TokenVector = Map<String, Pair<Float, List<Int>>>


class IdfComputer(private val documentCount: Int) {
    fun getIdf(containingDocumentCount: Int): Double {
        return ln(documentCount.toDouble() / (1 + containingDocumentCount))
    }

    fun getVectorTfIdf(retroIndex: RetroIndex, tokens: TokenVector): List<Double> {
        return tokens.map { (word, value) ->
            getIdf(retroIndex.forWord(word).count()) * value.first
        }.toList()
    }
}