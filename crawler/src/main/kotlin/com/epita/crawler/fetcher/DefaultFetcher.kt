package com.epita.crawler.fetcher

import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.annotation.Pure
import java.net.URL

class DefaultFetcher : Fetcher {

    @Pure
    @NotNull
    override fun fetch(@NotNull url: URL): String {
        return url.readText()
    }
}