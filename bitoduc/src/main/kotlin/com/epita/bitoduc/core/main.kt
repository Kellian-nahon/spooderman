package com.epita.bitoduc.core

import com.epita.bitoduc.api.Server
import io.javalin.Javalin

fun main(args: Array<String>) {
    val app = Javalin.create()
    Server.setup(app)
    app.start(7000)
}
