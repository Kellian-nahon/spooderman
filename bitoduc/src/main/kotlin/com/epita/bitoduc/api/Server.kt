package com.epita.bitoduc.api

import com.epita.bitoduc.broker.ClientId
import com.epita.bitoduc.broker.TopicId
import com.epita.bitoduc.utils.jsonHandler
import io.javalin.Javalin
import io.javalin.http.Context

data class SubscribeMessage(val clientId: ClientId, val topicId: TopicId)

data class SubscribeResponse(val topicId: TopicId)

object Server {
    private fun subscribe(message: SubscribeMessage): SubscribeResponse {
        return SubscribeResponse(message.topicId)
    }

    private fun publish(ctx: Context) {

    }

    private fun disconnect(ctx: Context) {

    }

    fun setup(app: Javalin) {
        app.post("/subscribe", jsonHandler(SubscribeMessage::class.java, Server::subscribe))
        app.post("/publish", Server::publish)
        app.post("/disconnect", Server::disconnect)
    }
}
