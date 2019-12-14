package com.epita.crawler.cleaners

import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.Pure
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class DomCleaner(): Cleaner, LogBean {

    @Pure
    override fun cleanup(input: String): String {
        val htmlDocument = Jsoup.parse(input)
        val title = htmlDocument.title()
        val body = htmlDocument.body().text()

        val cleanedText = "${title} ${body}"

        logger().trace("Cleaned document of title: ${title}")

        return "${htmlDocument.title()} ${htmlDocument.body().text()}"
    }
}