package com.epita.bitoduc.api.server

import com.epita.bitoduc.broker.*
import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Prototype
import com.epita.reussaure.provider.Singleton
import io.javalin.Javalin
import java.util.function.Supplier

fun main(args: Array<String>) {
    val reussaure = Reussaure {
        addProvider(Singleton(Broker::class.java, Supplier { DefaultBroker() }))
        addProvider(Singleton(Javalin::class.java, Supplier { Javalin.create() }))
        addProvider(Prototype(BitoducAPIServer::class.java, Supplier {
            BitoducAPIServer(
                instanceOf(Broker::class.java),
                instanceOf(Javalin::class.java)
            )
        }))
    }

    reussaure.instanceOf(BitoducAPIServer::class.java).start(7000)
}
