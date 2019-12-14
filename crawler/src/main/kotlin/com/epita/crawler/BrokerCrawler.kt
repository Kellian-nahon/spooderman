package com.epita.crawler

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerProducer
import com.epita.broker.core.PublicationType
import com.epita.crawler.core.Crawler
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.Topics
import com.epita.spooderman.commands.CrawlURLCommand
import com.epita.spooderman.events.CrawledURLEvent
import com.epita.spooderman.events.FoundURLEvent

class BrokerCrawler(private val consumer: BrokerConsumer,
                    private val producer: BrokerProducer,
                    private val crawler: Crawler) : LogBean{
    init {
        setup()
    }

    private fun onCrawlURLCommand(command: CrawlURLCommand) {
        val (text, urlSet) = crawler.crawl(command.url)
        producer.sendMessage(
            Topics.CrawledURLEvent.topicId,
            CrawledURLEvent(command.url, text),
            PublicationType.ONCE
        ) { response, error ->
            error?.let {
                logger().warn(it.toString())
            }
        }
        urlSet.forEach {
            producer.sendMessage(Topics.FoundURLEvent.topicId,
                FoundURLEvent(it),
                PublicationType.ONCE
            ) { response, error ->
                error?.let {
                    logger().warn(it.toString())
                }
            }
        }
    }

    private fun setup() {
        consumer.setHandler(
            Topics.CrawlURLCommand.topicId,
            CrawlURLCommand::class.java
        ) { onCrawlURLCommand(it) }
    }

    fun start(serverPort: Int) {
        consumer.start(serverPort)
    }
}