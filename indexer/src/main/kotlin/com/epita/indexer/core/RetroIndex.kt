package com.epita.indexer.core

import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.types.Document
import com.epita.spooderman.utils.MutableMultiMap
import com.epita.spooderman.utils.mutableMultiMapOf

class RetroIndex : LogBean {
    private val index: MutableMultiMap<String, Document> =
        mutableMultiMapOf()
    var documentsCount = 0

    fun addDocument(document: Document) {
        logger().info("Add a document")
        documentsCount += 1
        document.vector.keys.forEach { word ->
            index.addValue(word, document)
        }
    }

    fun forWord(word: String): List<Document> {
        return index.getOrDefault(word, arrayListOf())
    }
}
