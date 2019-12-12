package com.epita.bitoduc.core

import com.epita.bitoduc.api.Server
import com.epita.bitoduc.broker.*
import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Prototype
import com.epita.reussaure.provider.Singleton
import java.util.function.Supplier

fun main(args: Array<String>) {
    val reussaure = Reussaure {
        addProvider(Singleton(Broker::class.java, Supplier { DefaultBroker() }))
        addProvider(Prototype(Server::class.java, Supplier { Server(instanceOf(Broker::class.java))}))
    }

    reussaure.instanceOf(Server::class.java).start()
}
