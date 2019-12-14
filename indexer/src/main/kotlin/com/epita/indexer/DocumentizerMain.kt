package com.epita.indexer

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerHTTPClient
import com.epita.broker.api.client.BrokerProducer
import com.epita.broker.api.client.TestMessage
import com.epita.broker.core.PublicationType
import com.epita.indexer.tokenisation.*
import com.epita.indexer.vectorisation.Vectorizer
import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Singleton
import com.epita.spooderman.Topics
import com.epita.spooderman.commands.DocumentizeContentCommand
import com.epita.spooderman.events.DocumentizedContentEvent
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.Javalin
import java.net.URL
import java.util.function.Supplier

fun main(args: Array<String>) {

    if (args.size != 2 || args[1].toIntOrNull() != null)
        return
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
    ).flatMap { (key, value) ->
        value.map {
            Pair(it, key)
        }
    }.toTypedArray()
    )

    val reussaure = Reussaure {
        addProvider(Singleton(Vectorizer::class.java, Supplier { Vectorizer() }))
        addProvider(Singleton(Tokenizer::class.java, Supplier {
            Tokenizer(
                Splitter(delimiters),
                listOf(
                    LowercaseReducer(), StopWordReducer(stopWords),
                    StemmingReducer(suffixes), SynonymsReducer(synonyms)
                )
            )
        }))
        addProvider(Singleton(Indexer::class.java, Supplier {
            Indexer(
                instanceOf(Tokenizer::class.java), instanceOf(Vectorizer::class.java)

            )
        }))

        addProvider(Singleton(BrokerHTTPClient::class.java, Supplier { BrokerHTTPClient(URL(args[0])) }))
        addProvider(Singleton(BrokerConsumer::class.java, Supplier {
            BrokerConsumer(instanceOf(BrokerHTTPClient::class.java), instanceOf(Javalin::class.java))
        }))
        addProvider(Singleton(BrokerProducer::class.java, Supplier {
            BrokerProducer(instanceOf(BrokerHTTPClient::class.java))
        }))

    }
    val client = reussaure.instanceOf(BrokerConsumer::class.java)
    client.setHandler(Topics.DocumentizeContentCommand.topicId, DocumentizeContentCommand::class.java) {
        val document = reussaure.instanceOf(Indexer::class.java).documentize(it.content)
        reussaure.instanceOf(BrokerProducer::class.java).sendMessage(
            Topics.DocumentizedContentEvent.topicId, DocumentizedContentEvent(document), PublicationType.ONCE
        ) { _, _ -> }

    }
    client.start(args[1].toInt())
}
