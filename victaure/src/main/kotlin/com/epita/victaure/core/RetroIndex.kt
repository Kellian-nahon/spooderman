package com.epita.victaure.core

import com.epita.spooderman.utils.MutableMultiMap
import com.epita.spooderman.utils.mutableMultiMapOf

class RetroIndex {
    private val index: MutableMultiMap<String, Document> = mutableMultiMapOf()
    var documentsCount = 0

    fun addDocument(document: Document) {
        documentsCount += 1
        document.vector.keys.forEach { word ->
            index.addValue(word, document)
        }
    }

    fun forWord(word: String): List<Document> {
        return index.getOrDefault(word, arrayListOf())
    }
}
