package com.epita.indexer.core

import com.epita.broker.api.client.BrokerConsumer
import com.epita.spooderman.Topics
import com.epita.spooderman.commands.IndexDocumentCommand

class IndexBrokerWrapper(private val retroIndex: RetroIndex,
                         private val brokerConsumer: BrokerConsumer
) {
    init {
        setUp()
    }

    private fun setUp() {
        brokerConsumer.setHandler(Topics.IndexDocumentCommand.topicId, IndexDocumentCommand::class.java) {
            retroIndex.addDocument(it.document)

        }
    }

    fun start(port: Int) {
        brokerConsumer.start(port)
    }

}