package com.epita.urlvalidator.core

import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.Mutate
import java.net.HttpURLConnection
import java.net.URL

interface UrlValidator : LogBean {
    val validatedURLs: MutableSet<URL>

    @Mutate
    fun validateUrl(url: URL) : Boolean {
        logger().info("Checking url: ${url}")

        val cleanedURL = URL("${url.protocol}://${url.host}${url.path}")

        val inserted = validatedURLs.add(cleanedURL)
        if (!inserted) {
            logger().trace("URL was already treated")
            return false
        }

        var isValid = false
        with (cleanedURL.openConnection() as HttpURLConnection) {
            requestMethod = "HEAD"
            val contentType = headerFields["Content-Type"]
            if (this.responseCode < 300) {
                contentType?.let {
                    if (it.any { str -> str.contains("text/html") }) {
                        isValid = true
                    }
                }
            }
        }

        return isValid
    }
}
