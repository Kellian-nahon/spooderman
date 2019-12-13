package com.epita.butineur.fetcher

import java.net.URL

class DefaultFetcher : Fetcher {
    override fun fetch(url: URL): String {
        return url.readText()
    }
}