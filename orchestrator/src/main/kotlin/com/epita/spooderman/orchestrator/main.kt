package com.epita.spooderman.orchestrator

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerHTTPClient
import com.epita.broker.api.client.BrokerProducer
import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Singleton
import io.javalin.Javalin
import java.net.URL
import java.util.function.Supplier

fun main(args: Array<String>) {
    val brokerURL = args[0]
    val listeningPort = args[1].toInt()

    val reussaure = Reussaure {
        addProvider(Singleton(BrokerHTTPClient::class.java, Supplier { BrokerHTTPClient(URL(brokerURL)) }))
        addProvider(Singleton(Javalin::class.java, Supplier { Javalin.create() }))
        addProvider(Singleton(BrokerConsumer::class.java, Supplier {
            BrokerConsumer(instanceOf(BrokerHTTPClient::class.java), instanceOf(Javalin::class.java))
        }))
        addProvider(Singleton(BrokerProducer::class.java, Supplier {
            BrokerProducer(instanceOf(BrokerHTTPClient::class.java))
        }))
        addProvider(Singleton(Orchestrator::class.java, Supplier {
            Orchestrator(instanceOf(BrokerConsumer::class.java), instanceOf(BrokerProducer::class.java))
        }))
    }

    val orchestrator = reussaure.instanceOf(Orchestrator::class.java)
    orchestrator.start(listeningPort)
}