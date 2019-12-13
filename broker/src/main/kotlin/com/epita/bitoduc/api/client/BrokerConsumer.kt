package com.epita.bitoduc.api.client

import com.epita.bitoduc.api.dto.PublicationMessage
import com.epita.bitoduc.api.dto.SubscribeRequest
import com.epita.bitoduc.broker.ClientId
import com.epita.bitoduc.broker.TopicId
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.Javalin
import io.javalin.plugin.json.JavalinJson
import java.lang.Exception
import java.net.URL
import java.util.*

class BrokerConsumer(
    private val apiClient: BrokerHTTPClient,
    private val app: Javalin
) {
    private val handlers: MutableMap<Pair<ClientId, TopicId>, (PublicationMessage) -> Unit> = mutableMapOf()

    init {
        app.post("/callback") {ctx ->
            val message = JavalinJson.fromJson(ctx.body(), PublicationMessage::class.java)
            handleMessage(message)
        }
    }

    private fun handleMessage(message: PublicationMessage) {
        handlers[Pair(message.clientId, message.topicId)]?.let {handler ->
            handler(message)
        }
    }

    fun <MESSAGE_TYPE> setHandler(
        topicId: TopicId,
        messageType: Class<MESSAGE_TYPE>,
        clientId: ClientId = UUID.randomUUID().toString(),
        onMessage: (MESSAGE_TYPE) -> Unit
    ) {
        handlers[Pair(clientId, topicId)] = {
            onMessage(jacksonObjectMapper().readValue(it.message, messageType))
        }
    }

    fun start(serverPort: Int) {
        app.start(serverPort)
        val serverURL = app.server()?.server()?.uri ?: throw Exception("Could not get server URI")
        val callbackURL = URL("${serverURL}callback")

        handlers.keys.forEach {(clientId, topicId) ->
            apiClient.subscribe(SubscribeRequest(clientId, topicId, callbackURL)) {_, error ->
                // TODO: LOG ERROR
                error?.let {
                    println(it)
                }
            }
        }
    }
}