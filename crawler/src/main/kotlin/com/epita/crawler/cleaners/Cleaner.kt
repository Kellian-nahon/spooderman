package com.epita.crawler.cleaners

import com.epita.spooderman.annotation.Pure

interface Cleaner {
    @Pure
    fun cleanup(input: String): String
}