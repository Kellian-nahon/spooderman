package com.epita.spooderman.vectorisation

import com.epita.spooderman.core.DocumentVector


class Vectorizer {
    fun vectorize(words: List<String>): DocumentVector {
        val positionMap = mutableMapOf<String, ArrayList<Int>>()

        words.forEachIndexed { index, word ->
            if (!positionMap.containsKey(word))
                positionMap[word] = arrayListOf()
            positionMap[word]!!.add(index)
        }

        return mapOf(
            *positionMap.map { (word, positions) ->
                Pair(word, Pair(positions.count().toFloat() / words.count(), positions.toList()))
            }.toTypedArray()
        )
    }
}