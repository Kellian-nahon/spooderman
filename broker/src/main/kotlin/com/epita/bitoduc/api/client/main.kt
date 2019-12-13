package com.epita.bitoduc.api.client

import com.epita.bitoduc.core.PublicationType
import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Singleton
import io.javalin.Javalin
import java.net.URL
import java.util.function.Supplier

data class TestMessage(val value: String)

fun main(args: Array<String>) {
    val reussaure = Reussaure {
        addProvider(Singleton(Javalin::class.java, Supplier { Javalin.create() }))
        addProvider(Singleton(BrokerHTTPClient::class.java, Supplier { BrokerHTTPClient(URL(args[0])) }))
        addProvider(Singleton(BrokerConsumer::class.java, Supplier {
            BrokerConsumer(instanceOf(BrokerHTTPClient::class.java), instanceOf(Javalin::class.java))
        }))
        addProvider(Singleton(BrokerProducer::class.java, Supplier {
            BrokerProducer(instanceOf(BrokerHTTPClient::class.java))
        }))
    }

    val client = reussaure.instanceOf(BrokerConsumer::class.java)
    client.setHandler("some-topic", TestMessage::class.java) {
        println("Received message: ${it.value}")
    }
    client.start(9000)

    reussaure.instanceOf(BrokerProducer::class.java).sendMessage(
        "some-topic", TestMessage("Hello, World"), PublicationType.BROADCAST
    ) { _, _ -> }

}