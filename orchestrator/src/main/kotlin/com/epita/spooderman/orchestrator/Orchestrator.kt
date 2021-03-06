package com.epita.spooderman.orchestrator

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerProducer
import com.epita.broker.core.PublicationType
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.Topics
import com.epita.spooderman.commands.CrawlURLCommand
import com.epita.spooderman.commands.DocumentizeContentCommand
import com.epita.spooderman.commands.IndexDocumentCommand
import com.epita.spooderman.commands.ValidateURLCommand
import com.epita.spooderman.events.CrawledURLEvent
import com.epita.spooderman.events.DocumentizedContentEvent
import com.epita.spooderman.events.FoundURLEvent
import com.epita.spooderman.events.ValidatedURLEvent
import java.net.URL
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Orchestrator(private val consumer: BrokerConsumer, private val producer: BrokerProducer): LogBean {
    init {
        setup()
    }

    private val urlsToCrawl: Queue<URL> = LinkedList()
    private val executor= Executors.newScheduledThreadPool(1)

    private fun onValidatedURLEvent(event: ValidatedURLEvent) {
        logger().info("Received ValidatedURLEvent")
        urlsToCrawl.add(event.url)
    }

    private fun onCrawledURLEvent(event: CrawledURLEvent) {
        logger().info("Received CrawledURLEvent")
        producer.sendMessage(
            Topics.DocumentizeContentCommand.topicId,
            DocumentizeContentCommand(event.content, event.url),
            PublicationType.ONCE
        ) { _, error ->
            error?.let {
                logger().warn("Error while sending CrawledURLEvent: $error")
            } ?: run {
                logger().info("Sent DocumentizeContentCommand")
            }
        }
    }

    private fun onDocumentizedContentEvent(event: DocumentizedContentEvent) {
        logger().info("Received DocumentizedContentEvent")
        producer.sendMessage(
            Topics.IndexDocumentCommand.topicId,
            IndexDocumentCommand(event.document),
            PublicationType.ONCE
        ) { _, error ->
            error?.let {
                logger().warn("Error while sending IndexDocumentCommand: $error")
            } ?: run {
                logger().info("Sent IndexDocumentCommand")
            }
        }
    }

    private fun onFoundURLEvent(event: FoundURLEvent) {
        logger().info("Received FoundURLEvent")
        producer.sendMessage(
            Topics.ValidateURLCommand.topicId,
            ValidateURLCommand(event.url),
            PublicationType.ONCE
        ) { _, error ->
            error?.let {
                logger().warn("Error while sending ValidateURLCommand: $error")
            } ?: run {
                logger().info("Sent ValidateURLCommand")
            }
        }
    }


    private fun setup() {
        consumer.setHandler(
            Topics.ValidatedURLEvent.topicId,
            ValidatedURLEvent::class.java
        ) { onValidatedURLEvent(it) }

        consumer.setHandler(
            Topics.DocumentizedContentEvent.topicId,
            DocumentizedContentEvent::class.java
        ) { onDocumentizedContentEvent(it) }

        consumer.setHandler(
            Topics.CrawledURLEvent.topicId,
            CrawledURLEvent::class.java
        ) { onCrawledURLEvent(it) }

        consumer.setHandler(
            Topics.FoundURLEvent.topicId,
            FoundURLEvent::class.java
        ) { onFoundURLEvent(it) }
    }

    private fun crawlURL() {
        urlsToCrawl.poll()?.let{ url ->
            logger().info("Crawling URL: $url")
            producer.sendMessage(
                Topics.CrawlURLCommand.topicId,
                CrawlURLCommand(url),
                PublicationType.ONCE
            ) { _, error ->
                error?.let {
                    logger().warn("Error while sending CrawlURLCommand: $error")
                } ?: run {
                    logger().info("Sent CrawlURLCommand")
                }
            }
        }
    }

    fun start(serverPort: Int) {
        consumer.start(serverPort)
        executor.scheduleAtFixedRate( {
            logger().info("Executor called")
            crawlURL()
        }, 0, 500, TimeUnit.MILLISECONDS)
    }
}