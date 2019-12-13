package com.epita.urlvalidator

import com.epita.urlvalidator.core.UrlValidator
import java.net.URL

class DefaultUrlValidator() : UrlValidator {
    override val urlList: MutableSet<URL> = mutableSetOf()

    @Synchronized
    override fun validateUrl(url: URL): Boolean {
        return super.validateUrl(url)
    }
}
