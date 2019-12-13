package com.epita.crawler.fetcher

import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.annotation.Pure
import java.net.URL

interface Fetcher {

    @Pure
    @NotNull
    fun fetch(@NotNull url: URL) : String
}