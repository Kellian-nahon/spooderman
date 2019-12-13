package com.epita.crawler.cleaners

import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.annotation.Pure

interface Cleaner {
    @Pure
    @NotNull
    fun cleanup(@NotNull input: String): String
}