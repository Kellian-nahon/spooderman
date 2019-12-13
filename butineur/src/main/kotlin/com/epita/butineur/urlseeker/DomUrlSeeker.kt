package com.epita.butineur.urlseeker

import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.annotation.Pure
import org.jsoup.Jsoup
import java.net.URL

class DomUrlSeeker() : UrlSeeker {

    @NotNull
    @Pure
    override fun seekUrl(@NotNull url: URL, @NotNull input: String): Set<URL> {
        val htmlDocument = Jsoup.parse(input, url.toString())
        return htmlDocument.select("a")
            .map {link -> link.absUrl("href")}
            .filter { link -> link.isNotEmpty() }
            .map {href -> URL(href)}
            .toSet()
    }
}