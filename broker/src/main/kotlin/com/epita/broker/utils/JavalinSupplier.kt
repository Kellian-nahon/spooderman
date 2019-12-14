package com.epita.broker.utils

import io.javalin.Javalin
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import java.util.function.Supplier

object JavalinSupplier {
    fun get(listeningHostIp: String): Supplier<Javalin> {
        return Supplier {
            if (listeningHostIp.isNotEmpty()) {
                Javalin.create { config ->
                    config.server {
                        val server = Server()
                        val serverConnector = ServerConnector(server)
                        serverConnector.host = listeningHostIp
                        server.addConnector(serverConnector)
                        server
                    }
                }
            } else {
                Javalin.create()
            }
        }
    }
}