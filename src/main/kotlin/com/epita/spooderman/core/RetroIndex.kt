package com.epita.spooderman.core

class RetroIndex {
    private val index: Map<String, ArrayList<Document>> =
        mapOf<String, ArrayList<Document>>().withDefault { arrayListOf() }

    fun addDocument(document: Document) {
        document.vector.keys.forEach { word ->
            index[word]?.add(document)
        }
    }

    fun forWord(word: String): List<Document> {
        return index.getOrDefault(word, arrayListOf())
    }
}
