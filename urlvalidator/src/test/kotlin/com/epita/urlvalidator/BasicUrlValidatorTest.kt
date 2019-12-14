package com.epita.urlvalidator

import com.epita.urlvalidator.utils.UrlCleaner
import org.junit.Test
import java.net.URL
import kotlin.test.assertEquals

class BasicUrlValidatorTest {

    @Test
    fun removeAnchorTest() {
        val a1 = URL("http://google.com")
        val a2 = URL("http://google.com/test#title")
        val a3 = URL("http://google.com/test#")

        assertEquals(UrlCleaner.removeAnchor(a1), a1)
        assertEquals(UrlCleaner.removeAnchor(a2), URL("http://google.com/test"))
        assertEquals(UrlCleaner.removeAnchor(a3), URL("http://google.com/test"))
    }

}