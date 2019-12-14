package com.epita.urlvalidator.utils

import java.net.URL

object UrlCleaner {
    fun removeAnchor(url: URL) : URL {
        val urlString = url.toString()
        val anchorIndex = urlString.indexOf('#')
        if (anchorIndex <0) {
            return url
        }
        return URL(urlString.substring(0,  anchorIndex))
    }
}