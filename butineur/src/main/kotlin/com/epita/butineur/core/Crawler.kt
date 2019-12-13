package com.epita.butineur.core

import com.epita.butineur.cleaners.Cleaner
import com.epita.butineur.fetcher.Fetcher
import com.epita.butineur.urlseeker.UrlSeeker
import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.validator.Fault
import java.net.URL

interface Crawler {
    val fetcher: Fetcher
    val cleaner: Cleaner
    val urlSeeker: UrlSeeker

    @NotNull
    @Mutate
    fun crawl(@NotNull url: URL) : Pair<String, Set<URL>> {
        Fault.NULL.validate(url, "url")

        val fullText = fetcher.fetch(url)

        val text = cleaner.cleanup(fullText)
        val urls = urlSeeker.seekUrl(url, fullText)

        return Pair(text, urls)
    }
}