package com.epita.broker.api.client

import com.epita.broker.api.dto.DisconnectRequest
import com.epita.broker.api.dto.PublishRequest
import com.epita.broker.api.dto.SubscribeRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.URL

class BrokerHTTPClient(private val brokerURL: URL) {
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

    fun publish(publishRequest: PublishRequest, callback: Callback) {
        sendRequest("publish", publishRequest, callback)
    }

    fun subscribe(subscribeRequest: SubscribeRequest, callback: Callback) {
        sendRequest("subscribe", subscribeRequest, callback)
    }

    fun disconnect(disconnectRequest: DisconnectRequest, callback: Callback) {
        sendRequest("disconnect", disconnectRequest, callback)
    }
}

