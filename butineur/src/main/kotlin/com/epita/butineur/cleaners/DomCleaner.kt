package com.epita.butineur.cleaners

import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.annotation.Pure
import com.epita.spooderman.validator.Fault
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class DomCleaner(private val htmlDocument: Document): Cleaner {

    @Pure
    @NotNull
    override fun cleanup(@NotNull input: String): String {
        Fault.NULL.validate(input, "input")
        return "${htmlDocument.title()} ${htmlDocument.body().text()}"
    }
}