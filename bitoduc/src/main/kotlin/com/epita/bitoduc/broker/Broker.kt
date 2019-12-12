package com.epita.bitoduc.broker

import com.epita.bitoduc.core.PublicationType
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
    fun getClients(): MutableMap<ClientID, BrokerClient>
    fun sendMessageToURL(url: URL, topicID: TopicID, message: Message)

    fun clientHasTopic(client: BrokerClient, topicID: TopicID): Boolean {
        return client.topics.any(topicID::containsTopic)
    }

    fun subscribe(clientID: ClientID, topicID: TopicID, webhook: URL) {
        var client = getClients()[clientID]
        if (client == null) {
            client = BrokerClient(clientID, webhook, mutableSetOf(topicID))
            getClients()[clientID] = client
        } else {
            client.topics.add(topicID)
        }
    }

    fun disconnect(clientID: ClientID) {
        getClients().remove(clientID)
    }

    fun publish(topicID: TopicID, message: Message, type: PublicationType) {
        val subscribedClients = getClients().filterValues {
            clientHasTopic(it, topicID)
        }

        if (type == PublicationType.ONCE) {
            val client = subscribedClients.values.random()
            sendMessageToURL(client.url, topicID, message)
        } else {
            subscribedClients.values.forEach {client ->
                sendMessageToURL(client.url, topicID, message)
            }
        }
    }
}