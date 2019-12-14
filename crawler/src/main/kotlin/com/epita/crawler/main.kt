package com.epita.crawler

import com.epita.reussaure.core.Reussaure
import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerHTTPClient
import com.epita.broker.api.client.BrokerProducer
import com.epita.broker.utils.JavalinSupplier
import com.epita.crawler.cleaners.Cleaner
import com.epita.crawler.cleaners.DomCleaner
import com.epita.crawler.core.Crawler
import com.epita.crawler.fetcher.DefaultFetcher
import com.epita.crawler.fetcher.Fetcher
import com.epita.crawler.urlseeker.DomUrlSeeker
import com.epita.crawler.urlseeker.UrlSeeker
import com.epita.reussaure.provider.Singleton
import com.epita.spooderman.Topics
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import io.javalin.Javalin
import io.javalin.core.JavalinConfig
import io.javalin.core.JavalinServer
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.util.thread.QueuedThreadPool
import java.net.URL
import java.util.function.Supplier

class CLIArgs(parser: ArgParser) {
    val brokerURL by parser.storing("-u", "--url", help = "URL of the Message Broker")
    val listeningPort by parser.storing("-p", "--port", help = "The listening port for this service") {
        toInt()
    }.default(20200)
    val listeningHostIp by parser.storing("--host-ip", help ="The listening host for this service")
        .default { "" }
}

fun main(args: Array<String>) = mainBody {
    ArgParser(args, helpFormatter = DefaultHelpFormatter(
        """
            A service use to crawl URLs.${"\n"}
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
            addProvider(Singleton(BrokerCrawler::class.java, Supplier {
                BrokerCrawler(
                    instanceOf(BrokerConsumer::class.java),
                    instanceOf(BrokerProducer::class.java),
                    instanceOf(Crawler::class.java)
                )
            }))

            addProvider(Singleton(Crawler::class.java, Supplier {
                DefaultCrawler(
                    instanceOf(Fetcher::class.java),
                    instanceOf(Cleaner::class.java),
                    instanceOf(UrlSeeker::class.java)
                )
            }))

            addProvider(Singleton(Cleaner::class.java, Supplier { DomCleaner() }))
            addProvider(Singleton(Fetcher::class.java, Supplier { DefaultFetcher() }))
            addProvider(Singleton(UrlSeeker::class.java, Supplier { DomUrlSeeker() }))
        }

        val crawler = reussaure.instanceOf(BrokerCrawler::class.java)
        crawler.start(listeningPort)
    }
}