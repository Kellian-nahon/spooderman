package com.epita.tourniquet

import com.epita.tourniquet.core.UrlValidator
import java.net.URL

class Tourniquet() : UrlValidator {
    override val urlList: MutableSet<URL> = mutableSetOf()

    @Synchronized
    override fun validateUrl(url: URL): Boolean {
        return super.validateUrl(url)
    }
}
