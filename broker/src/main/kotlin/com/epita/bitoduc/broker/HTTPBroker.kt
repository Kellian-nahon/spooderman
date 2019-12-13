package com.epita.bitoduc.broker

import com.epita.bitoduc.api.dto.PublicationMessage
import com.epita.spooderman.utils.MutableMultiMap
import com.epita.spooderman.utils.mutableMultiMapOf
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class HTTPBroker : Broker {
    private val topics : MutableMultiMap<TopicId, BrokerClient> = mutableMultiMapOf()

    override fun getTopics(): MutableMultiMap<TopicId, BrokerClient> {
        return topics
    }

    override fun sendMessageToClient(client: BrokerClient, topicId: TopicId, message: String) {
        val payload = jacksonObjectMapper().writeValueAsString(PublicationMessage(client.id, topicId, message))
        khttp.async.post(client.url.toString(), data=payload) {
            // TODO: Log response
            println("URL: $url Status Code: $statusCode")
        }
    }
}