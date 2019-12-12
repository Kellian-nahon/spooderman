package com.epita.bitoduc.api

import com.epita.bitoduc.broker.Broker
import com.epita.bitoduc.api.dto.DisconnectRequest
import com.epita.bitoduc.api.dto.PublishRequest
import com.epita.bitoduc.api.dto.SubscribeRequest
import com.epita.bitoduc.utils.jsonHandler
import io.javalin.Javalin

class Server(private val broker: Broker) {

    private val JAVALIN_PORT = 7000

    private val app: Javalin = Javalin.create()

    init {
        setup()
    }

    private fun subscribe(message: SubscribeRequest) {
    }

    private fun publish(message: PublishRequest) {
        broker.publish(message.topicId, message.message, message.publishType)
    }

    private fun disconnect(message: DisconnectRequest) {

    }

    private fun setup() {
        app.post("/subscribe", jsonHandler(SubscribeRequest::class.java) { msg -> subscribe(msg)})
        app.post("/publish", jsonHandler(PublishRequest::class.java) { msg -> publish(msg)})
        app.post("/disconnect", jsonHandler(DisconnectRequest::class.java) { msg -> disconnect(msg)})
    }

    fun start() {
        app.start(JAVALIN_PORT)
    }
}
