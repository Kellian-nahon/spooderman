package com.epita.broker.broker

import com.epita.broker.core.PublicationType
import com.epita.spooderman.utils.MutableMultiMap
import java.net.URL

typealias ClientId = String
typealias Message = String

interface Broker {
    fun getTopics(): MutableMultiMap<TopicId, BrokerClient>
    fun sendMessageToClient(client: BrokerClient, topicId: TopicId, message: Message)

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
        val subscribedClients = getTopics()[topicId] ?: return

        if (type == PublicationType.ONCE) {
            val client = subscribedClients.random()
            sendMessageToClient(client, topicId, message)
        } else {
            subscribedClients.forEach { client ->
                sendMessageToClient(client, topicId, message)
            }
        }
    }
}
