package com.epita.crawler.fetcher

import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.annotation.Pure
import java.net.URL

class DefaultFetcher : Fetcher, LogBean {

    @Pure
    override fun fetch(url: URL): String {
        logger().info("Fetching url: ${url}")
        return url.readText()
    }
}