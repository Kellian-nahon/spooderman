package com.epita.broker.api.server

import com.epita.broker.broker.Broker
import com.epita.broker.api.dto.DisconnectRequest
import com.epita.broker.api.dto.PublishRequest
import com.epita.broker.api.dto.SubscribeRequest
import com.epita.broker.utils.jsonHandler
import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.annotation.Pure
import io.javalin.Javalin

class BrokerHTTPServer(private val broker: Broker, private val server: Javalin) {
    init {
        setup()
    }

    @Mutate
    private fun subscribe(message: SubscribeRequest) {
        broker.subscribe(message.clientId, message.topicId, message.callbackURL)
    }

    @Pure
    private fun publish(message: PublishRequest) {
        broker.publish(message.topicId, message.message, message.publishType)
    }

    @Mutate
    private fun disconnect(message: DisconnectRequest) {
        broker.disconnect(message.clientId)
    }

    @Mutate
    private fun setup() {
        server.post("/subscribe", jsonHandler(SubscribeRequest::class.java) { msg -> subscribe(msg)})
        server.post("/publish", jsonHandler(PublishRequest::class.java) { msg -> publish(msg)})
        server.post("/disconnect", jsonHandler(DisconnectRequest::class.java) { msg -> disconnect(msg)})
    }

    @Pure
    fun start(port: Int) {
        server.start(port)
    }
}
