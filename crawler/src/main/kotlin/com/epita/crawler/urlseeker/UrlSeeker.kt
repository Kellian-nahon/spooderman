package com.epita.crawler.urlseeker

import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.annotation.Pure
import java.net.URL

interface UrlSeeker {

    @NotNull
    @Pure
    fun seekUrl(@NotNull url: URL, @NotNull input: String) : Set<URL>
}