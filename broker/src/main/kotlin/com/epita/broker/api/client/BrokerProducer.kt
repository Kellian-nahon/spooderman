package com.epita.broker.api.client

import com.epita.broker.api.dto.PublishRequest
import com.epita.broker.broker.ClientId
import com.epita.spooderman.types.TopicId
import com.epita.broker.core.PublicationType
import com.epita.spooderman.annotation.Pure
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*

class BrokerProducer(
    private val apiClient: BrokerHTTPClient,
    private val clientId: ClientId = UUID.randomUUID().toString()
) {
    @Pure
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