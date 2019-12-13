package com.epita.urlvalidator.core

import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.annotation.NotNull
import java.net.URL

interface UrlValidator {
    val urlList: MutableSet<URL>

    @NotNull
    @Mutate
    fun validateUrl(@NotNull url: URL) : Boolean {
        return urlList.add(url)
    }
}
