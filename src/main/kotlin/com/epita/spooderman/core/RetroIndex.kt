package com.epita.spooderman.core

class RetroIndex {
    private val index: MutableMap<String, ArrayList<Document>> = mutableMapOf()

    fun addDocument(document: Document) {
        document.vector.keys.forEach { word ->
            if (!index.containsKey(word)) {
                index[word] = arrayListOf()
            }
            index[word]!!.add(document)
        }
    }

    fun forWord(word: String) {
        index.getOrDefault(word, arrayListOf())
    }
}
