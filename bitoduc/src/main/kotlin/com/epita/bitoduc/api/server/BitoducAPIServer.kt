package com.epita.bitoduc.api.server

import com.epita.bitoduc.broker.Broker
import com.epita.bitoduc.api.dto.DisconnectRequest
import com.epita.bitoduc.api.dto.PublicationMessage
import com.epita.bitoduc.api.dto.PublishRequest
import com.epita.bitoduc.api.dto.SubscribeRequest
import com.epita.bitoduc.utils.jsonHandler
import io.javalin.Javalin

class BitoducAPIServer(private val broker: Broker, private val server: Javalin) {
    init {
        setup()
    }

    private fun subscribe(message: SubscribeRequest) {
        broker.subscribe(message.clientId, message.topicId, message.callbackURL)
    }

    private fun publish(message: PublishRequest) {
        broker.publish(message.topicId, message.message, message.publishType)
    }

    private fun disconnect(message: DisconnectRequest) {
        broker.disconnect(message.clientId)
    }

    private fun setup() {
        server.post("/subscribe", jsonHandler(SubscribeRequest::class.java) { msg -> subscribe(msg)})
        server.post("/publish", jsonHandler(PublishRequest::class.java) { msg -> publish(msg)})
        server.post("/disconnect", jsonHandler(DisconnectRequest::class.java) { msg -> disconnect(msg)})
    }

    fun start(port: Int) {
        server.start(port)
    }
}
