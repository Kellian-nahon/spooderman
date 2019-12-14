package com.epita.spooderman.orchestrator

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerProducer
import com.epita.broker.core.PublicationType
import com.epita.spooderman.Topics
import com.epita.spooderman.commands.CrawlURLCommand
import com.epita.spooderman.commands.DocumentizeContentCommand
import com.epita.spooderman.commands.IndexDocumentCommand
import com.epita.spooderman.events.CrawledURLEvent
import com.epita.spooderman.events.DocumentizedContentEvent
import com.epita.spooderman.events.ValidatedURLEvent

class Orchestrator(private val consumer: BrokerConsumer, private val producer: BrokerProducer) {
    init {
        setup()
    }

    private fun onValidatedURLEvent(event: ValidatedURLEvent) {
        producer.sendMessage(
            Topics.CrawlURLCommand.topicId,
            CrawlURLCommand(event.url),
            PublicationType.ONCE
        ) { response, throwable ->
            // TODO: Log
        }
    }

    private fun onCrawledURLEvent(event: CrawledURLEvent) {
        producer.sendMessage(
            Topics.DocumentizeContentCommand.topicId,
            DocumentizeContentCommand(event.content),
            PublicationType.ONCE
        ) { response, throwable ->
            // TODO: Log
        }
    }

    private fun onDocumentizedContentEvent(event: DocumentizedContentEvent) {
        producer.sendMessage(
            Topics.IndexDocumentCommand.topicId,
            IndexDocumentCommand(event.document),
            PublicationType.ONCE
        ) { response, throwable ->
            // TODO: Log
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
    }

    fun start(serverPort: Int) {
        consumer.start(serverPort)
    }
}