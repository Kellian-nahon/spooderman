package com.epita.victaure

import com.epita.victaure.cleaners.DOMCleaner
import com.epita.victaure.core.TransportLayer
import org.junit.Test
import java.net.URL

class TransportLayerTest {
    @Test
    fun testGetUrl() {
        val transportLayer = TransportLayer(
            { URL("https://google.com").readText() },
            DOMCleaner()
        )
        val document = transportLayer.getDocument()

        assert(document.contains("Google"))
        assert(!document.contains("<div>"))
    }
}