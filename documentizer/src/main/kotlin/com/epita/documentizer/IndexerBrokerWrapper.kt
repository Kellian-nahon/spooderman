package com.epita.documentizer

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerProducer
import com.epita.broker.core.PublicationType
import com.epita.documentizer.core.Indexer
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.Topics
import com.epita.spooderman.commands.DocumentizeContentCommand
import com.epita.spooderman.events.DocumentizedContentEvent

class IndexerBrokerWrapper(private val indexer: Indexer,
                           private val brokerConsumer: BrokerConsumer,
                           private val brokerProducer: BrokerProducer): LogBean {
    init {
        setUp()
    }

    private fun setUp() {
        brokerConsumer.setHandler(Topics.DocumentizeContentCommand.topicId, DocumentizeContentCommand::class.java) {
            logger().trace("Handle DocumentizeContentCommand: ${it.url}")
            val document = indexer.documentize(it.content, it.url)
            brokerProducer.sendMessage(
                Topics.DocumentizedContentEvent.topicId, DocumentizedContentEvent(document), PublicationType.ONCE
            ) { _, error ->
                error?.let {
                    logger().error("An error occured when sending documentizedContentEvent: $error")
                }
           }
        }
    }

    fun start(port: Int) {
        logger().trace("Start on port: $port")
        brokerConsumer.start(port)
    }

}