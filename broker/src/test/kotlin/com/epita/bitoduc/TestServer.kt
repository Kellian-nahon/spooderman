package com.epita.bitoduc

import com.epita.bitoduc.broker.*
import org.junit.Test
import java.net.URL

class TestServer {
    @Test
    fun topicContainsTest() {
        assert("mti".containsTopic("mti"))
        assert("mti".containsTopic("mti/"))
        assert("mti".containsTopic("mti/toto"))
        assert("mti".containsTopic("mti/toto/2"))
        assert("mti/toto/2".containsTopic("mti/toto/2"))
        assert("mti/toto/2".containsTopic("mti/toto/2/1"))

        assert(!"mti".containsTopic("mti2"))
        assert(!"mti/1".containsTopic("mti/2"))
        assert(!"mti/1/2/3".containsTopic("mti/2/2/3"))
        assert(!"mti2".containsTopic("mti/1"))
    }

    @Test
    fun clientHasTopic() {
        val client = BrokerClient("1", URL("https://google.com"))
    }
}