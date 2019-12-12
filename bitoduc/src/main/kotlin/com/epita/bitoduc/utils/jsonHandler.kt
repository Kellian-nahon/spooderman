package com.epita.bitoduc.utils

import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJson


fun <REQUEST_TYPE, RESPONSE_TYPE> jsonHandler(messageType: Class<REQUEST_TYPE>, handler: (REQUEST_TYPE) -> RESPONSE_TYPE): (Context) -> Unit {
    return { ctx ->
        val body = JavalinJson.fromJson(ctx.body(), messageType)
        val response = handler(body)
        ctx.json(response as Any)
    }
}
