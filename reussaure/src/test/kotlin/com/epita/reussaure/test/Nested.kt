package com.epita.reussaure.test

import java.util.concurrent.Executors

class Nested(val service: TestService) {
    val threadPool = Executors.newCachedThreadPool()

    init {
        threadPool.submit {
            while (true) {
                service.pong()
                Thread.sleep(10000)
            }
        }
    }
}