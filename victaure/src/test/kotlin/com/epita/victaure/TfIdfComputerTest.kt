package com.epita.victaure

import com.epita.spooderman.core.Document
import com.epita.victaure.core.IdfComputer
import com.epita.spooderman.core.RetroIndex
import com.epita.spooderman.tokenisation.*
import com.epita.spooderman.vectorisation.Vectorizer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test

class TfIdfComputerTest {
    
    @Test
    fun testRankOfDocuments() {
        val mapper = jacksonObjectMapper()

        val delimiters: List<String> = mapper.readValue(
            Splitter::class.java.getResource("/punctuation_delimiters_en.json")
        )

        val stopWords: Set<String> = mapper.readValue(
            StopWordReducer::class.java.getResource("/stop_words_en.json")
        )

        val suffixes: Sequence<String> = mapper.readValue(
            StemmingReducer::class.java.getResource("/suffix_en.json")
        )

        val synonyms = hashMapOf(*mapper.readValue<Map<String, List<String>>>(
            SynonymsReducer::class.java.getResource("/synonyms_en.json")
        ).flatMap {(key, value) ->
            value.map {
                Pair(it, key)
            }
        }.toTypedArray())

        val tokenizer = Tokenizer(
            Splitter(delimiters),
            listOf(
                LowercaseReducer(), StopWordReducer(stopWords),
                StemmingReducer(suffixes), SynonymsReducer(synonyms)
            )
        )
        val vectorizer = Vectorizer()
        val content4 = "This id a test document to avoid full matches."
        val tokens4 = tokenizer.tokenize(content4)
        val doc4: Document = Document(content4, tokens4, vectorizer.vectorize(tokens4))
        val content5 = "This id another new text of document."
        val tokens5 = tokenizer.tokenize(content5)
        val doc5: Document = Document(content5, tokens5, vectorizer.vectorize(tokens5))
        val content1 = "The blue rabbit is fishing in a blue river."
        val tokens1 = tokenizer.tokenize(content1)
        val doc1: Document = Document(content1, tokens1, vectorizer.vectorize(tokens1))
        val content2 = "The red rabbit is cooking in a red river."
        val tokens2 = tokenizer.tokenize(content2)
        val doc2: Document = Document(content2, tokens2, vectorizer.vectorize(tokens2))
        val content3 = "The green rabbit is eating in a green river."
        val tokens3 = tokenizer.tokenize(content3)
        val doc3: Document = Document(content3, tokens3, vectorizer.vectorize(tokens3))
        val documents = listOf<Document>(doc1, doc2, doc3)
        val retroIndex = RetroIndex()
        documents.forEach { retroIndex.addDocument(it) }
        val idfComputer = IdfComputer(3)
        val queryVector = vectorizer.vectorize(tokenizer.tokenize("green rabbit"))
        val queryVectorTfIdf = idfComputer.getVectorTfIdf(retroIndex, queryVector)
        val queryVectorNormalized = idfComputer.normalize(queryVectorTfIdf)
        val vectorsOfDocuments = idfComputer.computeVectorsOfDocuments(retroIndex, queryVectorNormalized, documents)
        val rankList = idfComputer.getCosinusSimiliraty(queryVectorNormalized, vectorsOfDocuments)
        print(rankList)
    }
    
}