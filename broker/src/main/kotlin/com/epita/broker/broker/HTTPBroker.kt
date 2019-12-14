package com.epita.broker.broker

import com.epita.broker.api.dto.PublicationMessage
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.Pure
import com.epita.broker.broker.event_loggers.EventLogger
import com.epita.spooderman.types.TopicId
import com.epita.spooderman.utils.MutableMultiMap
import com.epita.spooderman.utils.mutableMultiMapOf
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.URL

class HTTPBroker(override val eventLogger: EventLogger) : Broker, LogBean {
    private val topics: MutableMultiMap<TopicId, BrokerClient> =
        mutableMultiMapOf()

    @Pure
    override fun getTopics(): MutableMultiMap<TopicId, BrokerClient> {
        return topics
    }

    @Pure
    override fun sendMessageToClient(clientURL: URL, message: PublicationMessage) {
        val payload = jacksonObjectMapper().writeValueAsString(message)
        khttp.async.post(clientURL.toString(), data = payload) {
            logger().trace("URL: ${url}, Status Code: ${statusCode}")
        }
    }
}