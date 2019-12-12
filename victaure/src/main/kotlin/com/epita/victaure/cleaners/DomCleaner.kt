package com.epita.victaure.cleaners

import org.jsoup.Jsoup

class DOMCleaner: Cleaner {
    override fun cleanup(input: String): String {
        val htmlDocument = Jsoup.parse(input);
        return "${htmlDocument.title()} ${htmlDocument.body().text()}"
    }
}