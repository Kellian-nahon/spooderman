package com.epita.crawler

import com.epita.reussaure.core.Reussaure
import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerHTTPClient
import com.epita.broker.api.client.BrokerProducer
import com.epita.crawler.cleaners.Cleaner
import com.epita.crawler.cleaners.DomCleaner
import com.epita.crawler.core.Crawler
import com.epita.crawler.fetcher.DefaultFetcher
import com.epita.crawler.fetcher.Fetcher
import com.epita.crawler.urlseeker.DomUrlSeeker
import com.epita.crawler.urlseeker.UrlSeeker
import com.epita.reussaure.provider.Singleton
import io.javalin.Javalin
import java.net.URL
import java.util.function.Supplier

fun main(args: Array<String>) {

    // TODO: Check args
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
        addProvider(Singleton(BrokerCrawler::class.java, Supplier {
            BrokerCrawler(instanceOf(BrokerConsumer::class.java),
                instanceOf(BrokerProducer::class.java),
                instanceOf(Crawler::class.java))
        }))

        addProvider(Singleton(Crawler::class.java, Supplier {
            DefaultCrawler(instanceOf(Fetcher::class.java),
                instanceOf(Cleaner::class.java),
                instanceOf(UrlSeeker::class.java))
        }))

        addProvider(Singleton(Cleaner::class.java, Supplier { DomCleaner() }))
        addProvider(Singleton(Fetcher::class.java, Supplier { DefaultFetcher() }))
        addProvider(Singleton(UrlSeeker::class.java, Supplier { DomUrlSeeker() }))
    }

    val crawler = reussaure.instanceOf(BrokerCrawler::class.java)
    crawler.start(listeningPort)
}