package com.epita.broker.api.client

import com.epita.broker.api.dto.DisconnectRequest
import com.epita.broker.api.dto.PublishRequest
import com.epita.broker.api.dto.SubscribeRequest
import com.epita.broker.exception.InvalidStatusException
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.Pure
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.URL

class BrokerHTTPClient(private val brokerURL: URL) : LogBean{
    private fun <PAYLOAD_TYPE> sendRequest(
        route: String,
        payload: PAYLOAD_TYPE,
        callback: Callback
    ) {
        khttp.async.post("$brokerURL/$route", data=jacksonObjectMapper().writeValueAsString(payload), onError =  {
            callback(null, this)
        }) {
            if (this.statusCode > 299 || this.statusCode < 200) {
                callback(this, InvalidStatusException(this))
            } else {
                callback(this, null)
            }
        }
    }

    @Pure
    fun publish(publishRequest: PublishRequest, callback: Callback) {
        logger().info("Sending request 'publish' from client: ${publishRequest.clientId} to topic ${publishRequest.topicId} of type ${publishRequest.publishType}")
        sendRequest("publish", publishRequest, callback)
    }

    @Pure
    fun subscribe(subscribeRequest: SubscribeRequest, callback: Callback) {
        logger().info("Sending request 'subscribe' from client: ${subscribeRequest.clientId} to topic ${subscribeRequest.topicId} to callback url: ${subscribeRequest.callbackURL}")
        sendRequest("subscribe", subscribeRequest, callback)
    }

    @Pure
    fun disconnect(disconnectRequest: DisconnectRequest, callback: Callback) {
        logger().info("Sending request 'disconnect' from client: ${disconnectRequest.clientId}" )
        sendRequest("disconnect", disconnectRequest, callback)
    }
}

