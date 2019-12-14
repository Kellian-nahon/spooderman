package com.epita.indexer

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerProducer
import com.epita.broker.core.PublicationType
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.Topics
import com.epita.spooderman.commands.DocumentizeContentCommand
import com.epita.spooderman.events.DocumentizedContentEvent
import kotlin.math.log

class IndexerBrokerWrapper(private val indexer: Indexer,
                           private val brokerConsumer: BrokerConsumer,
                           private val brokerProducer: BrokerProducer): LogBean {
    init {
        setUp()
    }

    private fun setUp() {
        brokerConsumer.setHandler(Topics.DocumentizeContentCommand.topicId, DocumentizeContentCommand::class.java) {
            logger().info("Handle DocumentizeContentCommand: %s", it.url.toString())
            val document = indexer.documentize(it.content, it.url)
            brokerProducer.sendMessage(
                Topics.DocumentizedContentEvent.topicId, DocumentizedContentEvent(document), PublicationType.ONCE
            ) { response, error ->
               logger().error("An error occured when sending documentizedContentEvent: %s", error)
           }
        }
    }

    fun start(port: Int) {
        logger().info("Start on port: %d", port)
        brokerConsumer.start(port)
    }

}