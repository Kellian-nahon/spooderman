package com.epita.butineur.urlseeker

import org.jsoup.Jsoup
import java.net.URL

class DomUrlSeeker() : UrlSeeker {
    override fun seekUrl(input: String): Set<URL> {
        val htmlDocument = Jsoup.parse(input)
        return htmlDocument.select("a")
            .map {link -> link.attr("abs:href")}
            .map {href -> URL(href)}
            .toSet()
    }
}