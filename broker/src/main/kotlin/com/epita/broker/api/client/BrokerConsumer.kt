package com.epita.broker.api.client

import com.epita.broker.api.dto.PublicationMessage
import com.epita.broker.api.dto.SubscribeRequest
import com.epita.broker.broker.ClientId
import com.epita.spooderman.Topics
import com.epita.spooderman.types.TopicId
import com.epita.broker.exception.UriNotAvailableException
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.Mutate
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.Javalin
import io.javalin.plugin.json.JavalinJson
import java.lang.Exception
import java.net.URL
import java.util.*

class BrokerConsumer(
    private val apiClient: BrokerHTTPClient,
    private val app: Javalin
) : LogBean{
    private val handlers: MutableMap<Pair<ClientId, TopicId>, (PublicationMessage) -> Unit> = mutableMapOf()

    init {
        app.post("/callback") {ctx ->
            val message = JavalinJson.fromJson(ctx.body(), PublicationMessage::class.java)
            handleMessage(message)
        }
    }

    @Mutate
    private fun handleMessage(message: PublicationMessage) {
        handlers[Pair(message.clientId, message.topicId)]?.let {handler ->
            handler(message)
        }
    }

    @Mutate
    fun <MESSAGE_TYPE> setHandler(
        topicId: TopicId,
        messageType: Class<MESSAGE_TYPE>,
        clientId: ClientId = UUID.randomUUID().toString(),
        onMessage: (MESSAGE_TYPE) -> Unit
    ) {
        handlers[Pair(clientId, topicId)] = {
            logger().info("Adding handler for client: ${clientId} and topic: ${topicId}")
            onMessage(jacksonObjectMapper().readValue(it.message, messageType))
        }
    }

    @Mutate
    fun start(serverPort: Int) {
        app.start(serverPort)
        logger().info("Server started on port: ${serverPort}")
        val serverURL = app.server()?.server()?.uri ?: throw UriNotAvailableException()
        val callbackURL = URL("${serverURL}callback")

        handlers.keys.forEach {(clientId, topicId) ->
            apiClient.subscribe(SubscribeRequest(clientId, topicId, callbackURL)) {_, error ->
                error?.let {
                    logger().warn(error.toString())
                }
            }
        }
    }
}