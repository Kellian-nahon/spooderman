package com.epita.broker.broker

import com.epita.broker.api.dto.PublicationMessage
import com.epita.broker.broker.event_loggers.EventLogger
import com.epita.broker.core.PublicationType
import com.epita.spooderman.types.TopicId
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.annotation.Pure
import com.epita.spooderman.utils.MutableMultiMap
import java.net.URL

typealias ClientId = String
typealias Message = String

interface Broker : LogBean {
    val eventLogger: EventLogger
    fun getTopics(): MutableMultiMap<TopicId, BrokerClient>
    fun sendMessageToClient(clientURL: URL, message: PublicationMessage)

    @Mutate
    fun subscribe(clientId: ClientId, topicId: TopicId, webhook: URL) {
        logger().info("Subscribing client: ${clientId} to topic: ${topicId}")

        val topics = getTopics()
        val topic = topics[topicId]
        if (topic == null) {
            logger().trace("Creating topic: ${topicId}")
            topics[topicId] = mutableListOf(BrokerClient(clientId, webhook))
        }
        else {
            topic.add(BrokerClient(clientId, webhook))
        }
    }

    @Mutate
    fun disconnect(clientId: ClientId) {
        getTopics().forEach { (_, clients) ->
            if (clients.removeIf { client ->
                client.id == clientId
            }) {
                logger().info("Client: ${clientId} was removed from topics")
            }
            else {
                logger().warn("No client : ${clientId} was subscribed")
            }
        }
    }

    @Pure
    fun publish(topicId: TopicId, message: Message, type: PublicationType) {
        logger().info("Sending message on topic: ${topicId} as ${type}")

        val subscribedClients = getTopics()[topicId]

        if (subscribedClients == null) {
            logger().warn("No clients subscribed on topic: ${topicId}")
        }

        subscribedClients?.let {
            if (subscribedClients.isEmpty()) {
                return
            }

            eventLogger.logEvent(topicId, message)

            if (type == PublicationType.ONCE) {
                val client = subscribedClients.random()
                sendMessageToClient(client.url, PublicationMessage(client.id, topicId, message))
            } else {
                subscribedClients.forEach { client ->
                    sendMessageToClient(client.url, PublicationMessage(client.id, topicId, message))
                }
            }
        }

    }
}
