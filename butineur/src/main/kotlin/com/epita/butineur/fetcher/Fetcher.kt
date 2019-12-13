package com.epita.butineur.fetcher

import java.net.URL

interface Fetcher {
    fun fetch(url: URL) : String
}