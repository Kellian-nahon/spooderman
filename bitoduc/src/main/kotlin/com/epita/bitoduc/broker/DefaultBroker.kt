package com.epita.bitoduc.broker

import com.epita.spooderman.utils.MutableMultiMap
import com.epita.spooderman.utils.mutableMultiMapOf
import java.net.URL

class DefaultBroker : Broker {
    private val topics : MutableMultiMap<TopicId, BrokerClient> = mutableMultiMapOf()

    override fun getTopics(): MutableMultiMap<TopicId, BrokerClient> {
        return topics
    }

    override fun sendMessageToURL(url: URL, topicId: TopicId, message: Message) {
        khttp.async.post(url.toString(), data=mapOf(
            Pair("topicId", topicId),
            Pair("message", "Message")
        )) {
            // TODO: Log response
            println("URL: $url Status Code: $statusCode")
        }
    }
}