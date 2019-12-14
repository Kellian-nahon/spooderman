package com.epita.broker.api.server

import com.epita.broker.broker.Broker
import com.epita.broker.broker.HTTPBroker
import com.epita.broker.broker.event_loggers.EventLogger
import com.epita.broker.broker.event_loggers.FileEventLogger
import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Prototype
import com.epita.reussaure.provider.Singleton
import com.epita.spooderman.Topics
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import io.javalin.Javalin
import java.io.File
import java.util.function.Supplier

class CLIArgs(parser: ArgParser) {
    val listeningPort by parser.storing("-p", "--port", help = "The listening port for this service") {
        toInt()
    }.default(20001)
}

fun main(args: Array<String>) {
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

    ArgParser(args, helpFormatter = DefaultHelpFormatter(
        """
            A service use to crawl URLs.${"\n"}
            Consuming on topic: ${Topics.ValidateURLCommand.topicId}${"\n"}
            Producing on topic: ${Topics.ValidatedURLEvent.topicId}${"\n"}
        """.trimIndent())
    ).parseInto(::CLIArgs).run {
        reussaure.instanceOf(BrokerHTTPServer::class.java).start(listeningPort)
    }
}
