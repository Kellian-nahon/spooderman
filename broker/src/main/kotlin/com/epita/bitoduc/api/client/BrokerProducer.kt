package com.epita.bitoduc.api.client

import com.epita.bitoduc.api.dto.PublishRequest
import com.epita.bitoduc.broker.ClientId
import com.epita.bitoduc.broker.TopicId
import com.epita.bitoduc.core.PublicationType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*

class BrokerProducer(
    private val apiClient: BrokerHTTPClient,
    private val clientId: ClientId = UUID.randomUUID().toString()
) {
    fun <MESSAGE_TYPE> sendMessage(topicId: TopicId, message: MESSAGE_TYPE, publicationType: PublicationType, callback: Callback) {
        val mapper = jacksonObjectMapper()
        val serializedMessage = mapper.writeValueAsString(message)
        apiClient.publish(PublishRequest(
            clientId,
            topicId,
            serializedMessage,
            publicationType
        ), callback)
    }
}