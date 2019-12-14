package com.epita.indexer.core

import com.epita.broker.api.client.BrokerConsumer
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.Topics
import com.epita.spooderman.commands.IndexDocumentCommand

class IndexBrokerWrapper(private val retroIndex: RetroIndex,
                         private val brokerConsumer: BrokerConsumer
) :LogBean {
    init {
        setUp()
    }

    private fun setUp() {
        brokerConsumer.setHandler(Topics.IndexDocumentCommand.topicId, IndexDocumentCommand::class.java) {
            logger().info("Handle DocumentizeContentCommand: ${it.document.url.toString()}")
            retroIndex.addDocument(it.document)
        }
    }

    fun start(port: Int) {
        logger().info("Start on port: %d", port)
        brokerConsumer.start(port)
    }

}