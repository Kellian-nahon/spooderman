package com.epita.bitoduc.api

import com.epita.bitoduc.api.dto.DisconnectRequest
import com.epita.bitoduc.api.dto.PublishRequest
import com.epita.bitoduc.api.dto.SubscribeRequest
import com.epita.bitoduc.broker.ClientId
import com.epita.bitoduc.broker.Message
import com.epita.bitoduc.broker.TopicId
import com.epita.bitoduc.core.PublicationType
import khttp.responses.Response
import java.net.URL

interface Client {
    val clientId: ClientId
    val responseURL: URL
    val brokerURL: URL

    fun <PAYLOAD_TYPE> sendRequest(
        route: String,
        payload: PAYLOAD_TYPE,
        onError: (Throwable.() -> Unit) = {},
        onResponse: (Response.() -> Unit) = {}
    ) {
        khttp.async.post("$brokerURL/$route", data=payload, onResponse = onResponse, onError = onError)
    }

    fun publish(topicId: TopicId, message: Message, type: PublicationType = PublicationType.BROADCAST) {
        sendRequest("publish", PublishRequest(clientId, topicId, message, type)) {
            // TODO: LOG TIS
            println("Published to $topicId -> Status Code: $statusCode")
        }
    }

    fun subscribe(topicId: TopicId) {
        sendRequest("subscribe", SubscribeRequest(clientId, topicId)) {
            // TODO: LOG TIS
            println("Subscribe to $topicId -> Status Code: $statusCode")
        }
    }

    fun disconnect() {
        sendRequest("disconnect", DisconnectRequest(clientId)) {
            // TODO: LOG TIS
            println("Disconnect -> Status Code: $statusCode")
        }
    }
}