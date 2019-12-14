package com.epita.crawler.urlseeker

import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.annotation.Pure
import org.jsoup.Jsoup
import java.net.URL

class DomUrlSeeker() : UrlSeeker, LogBean {

    @Pure
    override fun seekUrl(url: URL, input: String): Set<URL> {
        logger().info("Seeking urls on page: ${url}")
        val htmlDocument = Jsoup.parse(input, url.toString())

        val urlSet = htmlDocument.select("a")
            .map {link -> link.absUrl("href")}
            .filter { link -> link.isNotEmpty() }
            .map {href -> URL(href)}
            .toSet()

        logger().info("Fetched ${urlSet.count()} urls ")
        return urlSet
    }
}