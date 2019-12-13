package com.epita.butineur.cleaners

import java.net.URL

interface Cleaner {
    fun cleanup(input: String): String
}