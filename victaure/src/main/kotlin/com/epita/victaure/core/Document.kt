package com.epita.victaure.core

typealias DocumentContent = String
typealias DocumentVector = Map<String, Pair<Float, List<Int>>>

data class Document(val content: DocumentContent, val tokens: List<String>, val vector: DocumentVector)