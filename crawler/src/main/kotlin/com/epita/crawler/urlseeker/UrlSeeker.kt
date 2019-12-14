package com.epita.crawler.urlseeker

import com.epita.spooderman.annotation.Pure
import java.net.URL

interface UrlSeeker {

    @Pure
    fun seekUrl(url: URL, input: String) : Set<URL>
}