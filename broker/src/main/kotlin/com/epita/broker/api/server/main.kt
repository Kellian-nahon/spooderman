package com.epita.broker.api.server

import com.epita.broker.broker.*
import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Prototype
import com.epita.reussaure.provider.Singleton
import io.javalin.Javalin
import java.util.function.Supplier

fun main(args: Array<String>) {
    val reussaure = Reussaure {
        addProvider(Singleton(Broker::class.java, Supplier { HTTPBroker() }))
        addProvider(Singleton(Javalin::class.java, Supplier { Javalin.create() }))
        addProvider(Prototype(BrokerHTTPServer::class.java, Supplier {
            BrokerHTTPServer(
                instanceOf(Broker::class.java),
                instanceOf(Javalin::class.java)
            )
        }))
    }

    reussaure.instanceOf(BrokerHTTPServer::class.java).start(7000)
}
