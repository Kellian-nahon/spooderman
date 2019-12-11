package com.epita.spooderman.cleaners

import com.epita.spooderman.core.Document

interface Cleaner {
    fun cleanup(input: String): String
}