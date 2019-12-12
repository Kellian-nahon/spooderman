package com.epita.bitoduc.broker

import com.epita.bitoduc.core.PublicationType
import com.epita.spooderman.utils.MutableMultiMap
import java.net.URL

typealias ClientID = String
typealias TopicID = String
typealias Message = String

fun TopicID.containsTopic(other: TopicID): Boolean {
    val split = this.split("/")
    val otherSplit = other.split("/")

    for ((i, value) in split.withIndex()) {
        if (otherSplit[i] != value) {
            return false
        }
    }
    return true
}

interface Server {
    fun getTopics(): MutableMultiMap<TopicID, BrokerClient>
    fun sendMessageToURL(url: URL, topicID: TopicID, message: Message)

    fun subscribe(clientID: ClientID, topicID: TopicID, webhook: URL) {
        val topics = getTopics()
        val topic = topics[topicID]
        if (topic == null) {
            topics[topicID] = mutableListOf(BrokerClient(clientID, webhook))
        }
        else {
            topic.add(BrokerClient(clientID, webhook))
        }
    }

    fun disconnect(clientID: ClientID) {
        getTopics().forEach { (_, clients) ->
            clients.removeIf { client ->
                client.id == clientID
            }
        }
    }

    fun publish(topicID: TopicID, message: Message, type: PublicationType) {
        val subscribedClients = getTopics()[topicID]

        if (subscribedClients == null) {
            return
        }

        if (type == PublicationType.ONCE) {
            val client = subscribedClients.random()
            sendMessageToURL(client.url, topicID, message)
        } else {
            subscribedClients.forEach { client ->
                sendMessageToURL(client.url, topicID, message)
            }
        }
    }
}