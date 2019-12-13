package com.epita.broker.broker

import com.epita.broker.api.dto.PublicationMessage
import com.epita.broker.broker.event_loggers.EventLogger
import com.epita.spooderman.types.TopicId
import com.epita.spooderman.utils.MutableMultiMap
import com.epita.spooderman.utils.mutableMultiMapOf
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.URL

class HTTPBroker(override val eventLogger: EventLogger) : Broker {
    private val topics: MutableMultiMap<TopicId, BrokerClient> =
        mutableMultiMapOf()

    override fun getTopics(): MutableMultiMap<TopicId, BrokerClient> {
        return topics
    }

    override fun sendMessageToClient(clientURL: URL, message: PublicationMessage) {
        val payload = jacksonObjectMapper().writeValueAsString(message)
        khttp.async.post(clientURL.toString(), data = payload) {
            // TODO: Log response
            println("URL: $url Status Code: $statusCode")
        }
    }
}