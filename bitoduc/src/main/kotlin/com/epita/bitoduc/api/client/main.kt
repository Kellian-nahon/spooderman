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
        addProvider(Singleton(BitoducAPIClient::class.java, Supplier { BitoducAPIClient(URL(args[0])) }))
        addProvider(Singleton(BitoducClient::class.java, Supplier {
            BitoducClient(instanceOf(BitoducAPIClient::class.java), instanceOf(Javalin::class.java))
        }))
        addProvider(Singleton(BitoducProducer::class.java, Supplier {
            BitoducProducer(instanceOf(BitoducAPIClient::class.java))
        }))
    }

    val app = reussaure.instanceOf(Javalin::class.java)
    app.server()?.serverPort = 8000
    val client = reussaure.instanceOf(BitoducClient::class.java)
    client.setHandler("some-topic", TestMessage::class.java) {
        println("Received message: ${it.value}")
    }
    client.start()

    reussaure.instanceOf(BitoducProducer::class.java).sendMessage(
        "some-topic", TestMessage("Hello, World"), PublicationType.BROADCAST
    ) { _, _ -> }

}