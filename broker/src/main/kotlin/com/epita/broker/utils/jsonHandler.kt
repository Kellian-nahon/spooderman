package com.epita.broker.utils

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJson


fun <REQUEST_TYPE, RESPONSE_TYPE> jsonHandler(messageType: Class<REQUEST_TYPE>, handler: (REQUEST_TYPE) -> RESPONSE_TYPE): (Context) -> Unit {
    return { ctx ->
        val body: REQUEST_TYPE
        try {
            body = JavalinJson.fromJson(ctx.body(), messageType)
        } catch (t: Throwable) {
            throw BadRequestResponse()
        }
        val response = handler(body)
        if (response != Unit) {
            ctx.json(response as Any)
        }
    }
}
