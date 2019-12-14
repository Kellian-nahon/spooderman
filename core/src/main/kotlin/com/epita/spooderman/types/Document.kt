package com.epita.spooderman.types

import java.net.URL

typealias DocumentContent = String
typealias DocumentVector = Map<String, Pair<Float, List<Int>>>

data class Document(val url: URL, val content: DocumentContent, val tokens: List<String>, val vector: DocumentVector)