package com.epita.crawler.cleaners

import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.annotation.Pure
import com.epita.spooderman.validator.Fault
import org.jsoup.nodes.Document

class DomCleaner(private val htmlDocument: Document): Cleaner, LogBean {

    @Pure
    @NotNull
    override fun cleanup(@NotNull input: String): String {
        val title = htmlDocument.title()
        val body = htmlDocument.body().text()

        val cleanedText = "${title} ${body}"

        logger().info("Cleaned document of title: ${title}")

        return "${htmlDocument.title()} ${htmlDocument.body().text()}"
    }
}