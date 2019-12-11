package com.epita.spooderman

import com.epita.spooderman.cleaners.DOMCleaner
import com.epita.spooderman.core.TransportLayer
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

        assert(document.content.contains("Google"))
        assert(!document.content.contains("<div>"))
    }
}