package com.epita.crawler

import com.epita.crawler.urlseeker.DomUrlSeeker
import org.junit.Test
import java.io.File
import java.net.URL
import kotlin.test.assertEquals

class UrlSeekerTest {

    @Test
    fun testUrlSeeker() {

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource("website.html").file)
        val html = file.readText()

        val urlSeeker = DomUrlSeeker()
        val urlSet = urlSeeker.seekUrl(URL("https://website.com"), html)

        assertEquals(urlSet, setOf(URL("https://website.com"), URL("https://website.com/test")))
    }
}