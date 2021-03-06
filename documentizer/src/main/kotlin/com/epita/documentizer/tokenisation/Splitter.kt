package com.epita.documentizer.tokenisation

import java.util.function.Function

class Splitter(private val delimiters: List<String>): Function<String, List<String>> {
    override fun apply(text: String): List<String> {
        return text.split(*delimiters.toTypedArray(), ignoreCase = true)
    }
}
