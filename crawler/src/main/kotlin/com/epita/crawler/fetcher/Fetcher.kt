package com.epita.crawler.fetcher

import com.epita.spooderman.annotation.Pure
import java.net.URL

interface Fetcher {

    @Pure
    fun fetch(url: URL) : String
}