package com.epita.indexer

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerProducer
import com.epita.broker.core.PublicationType
import com.epita.spooderman.Topics
import com.epita.spooderman.commands.DocumentizeContentCommand
import com.epita.spooderman.events.DocumentizedContentEvent

class IndexerBrokerWrapper(private val indexer: Indexer,
                           private val brokerConsumer: BrokerConsumer,
                           private val brokerProducer: BrokerProducer) {
    init {
        setUp()
    }

    private fun setUp() {
        brokerConsumer.setHandler(Topics.DocumentizeContentCommand.topicId, DocumentizeContentCommand::class.java) {
            val document = indexer.documentize(it.content)
           brokerProducer.sendMessage(
                Topics.DocumentizedContentEvent.topicId, DocumentizedContentEvent(document), PublicationType.ONCE
            ) { _, _ -> }
        }
    }

    fun start(port: Int) {
        brokerConsumer.start(port)
    }

}