package com.epita.broker.api.server

import com.epita.broker.broker.Broker
import com.epita.broker.broker.HTTPBroker
import com.epita.broker.broker.event_loggers.EventLogger
import com.epita.broker.broker.event_loggers.FileEventLogger
import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Prototype
import com.epita.reussaure.provider.Singleton
import io.javalin.Javalin
import java.io.File
import java.util.function.Supplier

fun main(args: Array<String>) {
    val port = args[0].toInt()
    val reussaure = Reussaure {
        val eventLogFile = File(args.getOrNull(1) ?: "event_log.txt")
        addProvider(Singleton(EventLogger::class.java, Supplier { FileEventLogger(eventLogFile.outputStream()) }))
        addProvider(Singleton(Broker::class.java, Supplier { HTTPBroker(instanceOf(EventLogger::class.java)) }))
        addProvider(Singleton(Javalin::class.java, Supplier { Javalin.create() }))
        addProvider(Prototype(BrokerHTTPServer::class.java, Supplier {
            BrokerHTTPServer(
                instanceOf(Broker::class.java),
                instanceOf(Javalin::class.java)
            )
        }))
    }

    reussaure.instanceOf(BrokerHTTPServer::class.java).start(port)
}
