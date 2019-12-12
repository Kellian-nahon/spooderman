package com.epita.victaure

import com.epita.victaure.tokenisation.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test
import kotlin.test.assertEquals

class TokenizerTest {
    @Test
    fun testTokenisation() {

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

        val tokens = tokenizer.tokenize("The blue rabbit is fishing in a blue river.")
        assertEquals(listOf("blue", "rabbit", "fish", "blue", "river"), tokens)
    }




}
