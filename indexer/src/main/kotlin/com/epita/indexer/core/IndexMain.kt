package com.epita.indexer.core

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerHTTPClient
import com.epita.indexer.controller.ComputeSimilarityController
import com.epita.indexer.tokenisation.*
import com.epita.indexer.vectorisation.Vectorizer
import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Singleton
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.Javalin
import java.net.URL
import java.util.function.Supplier


fun main(args: Array<String>) {
    if (args.size != 3 || args[1].toIntOrNull() == null || args[2].toIntOrNull() == null)
        return
    val brokerURL = args[0]
    val listeningPortEventBus = args[1].toInt()
    val listeningPortServer = args[2].toInt()
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
        addProvider(Singleton(SimilarityComputer::class.java, Supplier { DefaultSimilarityComputer(
            instanceOf(RetroIndex::class.java))
        }))
        addProvider(Singleton(Querying::class.java, Supplier { DefaultQuerying(
            instanceOf(Tokenizer::class.java), instanceOf(Vectorizer::class.java), instanceOf(SimilarityComputer::class.java))
        }))
        addProvider(Singleton(Javalin::class.java, Supplier { Javalin.create() }))
        addProvider(Singleton(ComputeSimilarityController::class.java, Supplier { ComputeSimilarityController(
            instanceOf(Querying::class.java), instanceOf(Javalin::class.java))
        }))

        addProvider(Singleton(BrokerHTTPClient::class.java, Supplier { BrokerHTTPClient(URL(brokerURL)) }))
        addProvider(Singleton(BrokerConsumer::class.java, Supplier {
            BrokerConsumer(instanceOf(BrokerHTTPClient::class.java), instanceOf(Javalin::class.java))
        }))

        addProvider(Singleton(IndexBrokerWrapper::class.java, Supplier { IndexBrokerWrapper(
            instanceOf(RetroIndex::class.java),
            instanceOf(BrokerConsumer::class.java))
        }))
    }
    val client = reussaure.instanceOf(BrokerConsumer::class.java)
    client.start(listeningPortEventBus)
    reussaure.instanceOf(ComputeSimilarityController::class.java).start(listeningPortServer)

}
