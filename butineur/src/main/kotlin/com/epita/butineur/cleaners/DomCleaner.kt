package com.epita.butineur.cleaners

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class DomCleaner(private val htmlDocument: Document): Cleaner {
    override fun cleanup(input: String): String {
        return "${htmlDocument.title()} ${htmlDocument.body().text()}"
    }
}