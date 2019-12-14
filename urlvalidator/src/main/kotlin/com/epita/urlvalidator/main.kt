package com.epita.urlvalidator

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerHTTPClient
import com.epita.broker.api.client.BrokerProducer
import com.epita.broker.utils.JavalinSupplier
import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.provider.Singleton
import com.epita.spooderman.Topics
import com.xenomachina.argparser.*
import io.javalin.Javalin
import java.net.URL
import java.util.function.Supplier

class CLIArgs(parser: ArgParser) {
    val brokerURL by parser.storing("-u", "--url", help = "URL of the Message Broker")
    val listeningPort by parser.storing("-p", "--port", help = "The listening port for this service") {
        toInt()
    }.default(20300)
    val listeningHostIp by parser.storing("--host", help ="The listening host for this service")
        .default("")
}

fun main(args: Array<String>) = mainBody {
    ArgParser(args, helpFormatter = DefaultHelpFormatter(
        """
            A service use to validate incoming urls${"\n"}
            Consuming on topic: ${Topics.ValidateURLCommand.topicId}${"\n"}
            Producing on topic: ${Topics.ValidatedURLEvent.topicId}${"\n"}
        """.trimIndent())
    ).parseInto(::CLIArgs).run {
        val reussaure = Reussaure {
            addProvider(Singleton(BrokerHTTPClient::class.java, Supplier { BrokerHTTPClient(URL(brokerURL)) }))
            addProvider(Singleton(Javalin::class.java, JavalinSupplier.get(listeningHostIp)))
            addProvider(Singleton(BrokerConsumer::class.java, Supplier {
                BrokerConsumer(instanceOf(BrokerHTTPClient::class.java), instanceOf(Javalin::class.java))
            }))
            addProvider(Singleton(BrokerProducer::class.java, Supplier {
                BrokerProducer(instanceOf(BrokerHTTPClient::class.java))
            }))
            addProvider(Singleton(DefaultUrlValidator::class.java, Supplier {
                DefaultUrlValidator(instanceOf(BrokerConsumer::class.java), instanceOf(BrokerProducer::class.java))
            }))
        }

        val urlValidator = reussaure.instanceOf(DefaultUrlValidator::class.java)
        urlValidator.start(listeningPort)
    }
}