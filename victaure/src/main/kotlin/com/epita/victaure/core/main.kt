package com.epita.victaure.core

import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Singleton
import com.epita.victaure.Controller.ComputeSimilarityController
import com.epita.victaure.tokenisation.*
import com.epita.victaure.vectorisation.Vectorizer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.function.Supplier


fun main(args: Array<String>) {
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

    val reussaure = Reussaure {
        addProvider(Singleton(Vectorizer::class.java, Supplier { Vectorizer() }))
        addProvider(Singleton(Tokenizer::class.java, Supplier { Tokenizer(Splitter(delimiters),
            listOf(
                LowercaseReducer(), StopWordReducer(stopWords),
                StemmingReducer(suffixes), SynonymsReducer(synonyms)
            )) }))
        addProvider(Singleton(RetroIndex::class.java, Supplier { RetroIndex() }))
        addProvider(Singleton(SimilarityComputer::class.java, Supplier { SimilarityComputer(
            instanceOf(RetroIndex::class.java)
        ) }))
        addProvider(Singleton(ComputeSimilarityController::class.java, Supplier { ComputeSimilarityController(
            instanceOf(Tokenizer::class.java), instanceOf(Vectorizer::class.java), instanceOf(SimilarityComputer::class.java), instanceOf(RetroIndex::class.java)) }))
    }

}
