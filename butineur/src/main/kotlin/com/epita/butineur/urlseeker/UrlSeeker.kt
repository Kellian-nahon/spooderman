package com.epita.butineur.urlseeker

import java.net.URL

interface UrlSeeker {
    fun seekUrl(input: String) : Set<URL>
}