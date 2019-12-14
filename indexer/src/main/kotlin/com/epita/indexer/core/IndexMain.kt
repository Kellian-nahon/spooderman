package com.epita.indexer.core

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerHTTPClient
import com.epita.indexer.controller.ComputeSimilarityController
import com.epita.indexer.tokenisation.*
import com.epita.indexer.vectorisation.Vectorizer
import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Prototype
import com.epita.reussaure.provider.Singleton
import com.epita.spooderman.Topics
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import io.javalin.Javalin
import java.net.URL
import java.util.function.Supplier

class CLIArgs(parser: ArgParser) {
    val brokerURL by parser.storing("-u", "--url", help = "URL of the Message Broker")
    val listeningPort by parser.storing("-p", "--port", help = "The listening port for this service") {
        toInt()
    }.default(20400)
    val serverPort by parser.storing("--server-port", help = "The HTTP API port") {
        toInt()
    }.default(20000)
}

fun main(args: Array<String>) = mainBody {
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


    ArgParser(args, helpFormatter = DefaultHelpFormatter(
        """
            A service use to index documents.${"\n"}
            Consuming on topic: ${Topics.ValidateURLCommand.topicId}${"\n"}
            Producing on topic: ${Topics.ValidatedURLEvent.topicId}${"\n"}
        """.trimIndent())
    ).parseInto(::CLIArgs).run {

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
            addProvider(Singleton(RetroIndex::class.java, Supplier { RetroIndex() }))
            addProvider(Singleton(SimilarityComputer::class.java, Supplier {
                DefaultSimilarityComputer(
                    instanceOf(RetroIndex::class.java)
                )
            }))
            addProvider(Singleton(Querying::class.java, Supplier {
                DefaultQuerying(
                    instanceOf(Tokenizer::class.java),
                    instanceOf(Vectorizer::class.java),
                    instanceOf(SimilarityComputer::class.java)
                )
            }))
            addProvider(Prototype(Javalin::class.java, Supplier { Javalin.create() }))
            addProvider(Singleton(ComputeSimilarityController::class.java, Supplier {
                ComputeSimilarityController(
                    instanceOf(Querying::class.java), instanceOf(Javalin::class.java)
                )
            }))

            addProvider(Singleton(BrokerHTTPClient::class.java, Supplier { BrokerHTTPClient(URL(brokerURL)) }))
            addProvider(Singleton(BrokerConsumer::class.java, Supplier {
                BrokerConsumer(instanceOf(BrokerHTTPClient::class.java), instanceOf(Javalin::class.java))
            }))

            addProvider(Singleton(IndexBrokerWrapper::class.java, Supplier {
                IndexBrokerWrapper(
                    instanceOf(RetroIndex::class.java),
                    instanceOf(BrokerConsumer::class.java)
                )
            }))
        }
        val client = reussaure.instanceOf(BrokerConsumer::class.java)
        client.start(listeningPort)
        reussaure.instanceOf(ComputeSimilarityController::class.java).start(serverPort)
    }

}
