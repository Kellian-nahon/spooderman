package com.epita.bitoduc.api

import com.epita.bitoduc.broker.Broker
import com.epita.bitoduc.broker.ClientId
import com.epita.bitoduc.broker.TopicId
import com.epita.bitoduc.core.PublicationType
import com.epita.bitoduc.utils.jsonHandler
import io.javalin.Javalin
import io.javalin.http.Context

data class SubscribeMessage(val clientId: ClientId, val topicId: TopicId)

data class SubscribeResponse(val topicId: TopicId)

data class PublishMessage(val clientId: ClientId, val topicId: TopicId,
                          val type: PublicationType, val content: String)

class Server(private val broker: Broker) {

    private val JAVALIN_PORT = 7000

    private val app: Javalin = Javalin.create()

    init {
        setup()
    }

    private fun subscribe(message: SubscribeMessage): SubscribeResponse {
        return SubscribeResponse(message.topicId)
    }

    private fun publish(message: PublishMessage) {
        broker.publish(message.topicId, message.content, message.type)
    }

    private fun disconnect(ctx: Context) {

    }

    private fun setup() {
        app.post("/subscribe", jsonHandler(SubscribeMessage::class.java) { msg -> subscribe(msg)})
        app.post("/publish", jsonHandler(PublishMessage::class.java) { msg -> publish(msg)})
        app.post("/disconnect") { ctx -> disconnect(ctx)}
    }

    fun start() {
        app.start(JAVALIN_PORT)
    }
}
