package com.epita.crawler.core

import com.epita.crawler.cleaners.Cleaner
import com.epita.crawler.fetcher.Fetcher
import com.epita.crawler.urlseeker.UrlSeeker
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.Mutate
import java.net.URL

interface Crawler : LogBean{
    val fetcher: Fetcher
    val cleaner: Cleaner
    val urlSeeker: UrlSeeker

    @Mutate
    fun crawl(url: URL) : Pair<String, Set<URL>> {
        logger().info("Crawling url: ${url}")

        val fullText = fetcher.fetch(url)

        val text = cleaner.cleanup(fullText)
        val urls = urlSeeker.seekUrl(url, fullText)

        return Pair(text, urls)
    }
}