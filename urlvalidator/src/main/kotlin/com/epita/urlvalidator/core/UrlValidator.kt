package com.epita.urlvalidator.core

import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.Mutate
import com.epita.urlvalidator.utils.UrlCleaner
import java.net.URL

interface UrlValidator : LogBean {
    val urlList: MutableSet<URL>

    @Mutate
    fun validateUrl(url: URL) : Boolean {
        logger().trace("Checking url: ${url}")

        val urlNoAnchor = UrlCleaner.removeAnchor(url)

        var isInserted = urlList.add(urlNoAnchor)

        if (isInserted) {
            logger().info("Validated url: ${url}")
        }
        else {
            logger().info("Refused url: ${url}")
        }

        return isInserted
    }
}
