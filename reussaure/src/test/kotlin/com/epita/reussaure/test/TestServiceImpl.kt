package com.epita.reussaure.test

class TestServiceImpl : TestService {
    override fun pong(): String = "pong"

    override fun ping() = println("ping")

}