package com.epita.bitoduc.broker

import com.epita.spooderman.utils.MutableMultiMap
import com.epita.spooderman.utils.mutableMultiMapOf
import java.net.URL

class DefaultBroker : Broker {
    private val topics : MutableMultiMap<TopicId, BrokerClient> = mutableMultiMapOf()

    override fun getTopics(): MutableMultiMap<TopicId, BrokerClient> {
        return topics
    }

    override fun sendMessageToURL(url: URL, topicID: TopicId, message: Message) {

    }

}