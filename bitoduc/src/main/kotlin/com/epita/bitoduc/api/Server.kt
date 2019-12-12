package com.epita.bitoduc.api

import com.epita.bitoduc.broker.TopicID
import com.epita.bitoduc.utils.jsonHandler
import io.javalin.Javalin
import io.javalin.http.Context

// TODO: Fix ID
data class SubscribeMessage(val clientID: String, val topicID: String)

data class SubscribeResponse(val topicID: TopicID)

object Server {
    private fun subscribe(message: SubscribeMessage): SubscribeResponse {
        return SubscribeResponse(message.topicID)
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