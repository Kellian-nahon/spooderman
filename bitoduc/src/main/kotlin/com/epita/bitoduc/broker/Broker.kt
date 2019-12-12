package com.epita.bitoduc.broker

import com.epita.bitoduc.core.PublicationType
import com.epita.spooderman.utils.MutableMultiMap
import java.net.URL

typealias ClientId = String
typealias TopicId = String
typealias Message = String

fun TopicId.containsTopic(other: TopicId): Boolean {
    val split = this.split("/")
    val otherSplit = other.split("/")

    for ((i, value) in split.withIndex()) {
        if (otherSplit[i] != value) {
            return false
        }
    }
    return true
}

interface Broker {
    fun getTopics(): MutableMultiMap<TopicId, BrokerClient>
    fun sendMessageToURL(url: URL, topicID: TopicId, message: Message)

    fun subscribe(clientId: ClientId, topicId: TopicId, webhook: URL) {
        val topics = getTopics()
        val topic = topics[topicId]
        if (topic == null) {
            topics[topicId] = mutableListOf(BrokerClient(clientId, webhook))
        }
        else {
            topic.add(BrokerClient(clientId, webhook))
        }
    }

    fun disconnect(clientId: ClientId) {
        getTopics().forEach { (_, clients) ->
            clients.removeIf { client ->
                client.id == clientId
            }
        }
    }

    fun publish(topicId: TopicId, message: Message, type: PublicationType) {
        val subscribedClients = getTopics()[topicId]

        if (subscribedClients == null) {
            return
        }

        if (type == PublicationType.ONCE) {
            val client = subscribedClients.random()
            sendMessageToURL(client.url, topicId, message)
        } else {
            subscribedClients.forEach { client ->
                sendMessageToURL(client.url, topicId, message)
            }
        }
    }
}
