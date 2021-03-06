package com.epita.documentizer.vectorisation

import com.epita.spooderman.types.DocumentVector
import com.epita.spooderman.utils.mutableMultiMapOf


class Vectorizer {
    fun vectorize(words: List<String>): DocumentVector {
        val positionMap = mutableMultiMapOf<String, Int>()

        words.forEachIndexed { index, word ->
            positionMap.addValue(word, index)
        }

        return mapOf(
            *positionMap.map { (word, positions) ->
                Pair(word, Pair(positions.count().toFloat() / words.count(), positions.toList()))
            }.toTypedArray()
        )
    }
}