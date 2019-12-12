package com.epita.victaure

import com.epita.victaure.vectorisation.Vectorizer
import org.junit.Test
import kotlin.test.assertEquals

class VectorizerTest {

    @Test
    fun testVectorizer() {
        val dict = Vectorizer().vectorize(listOf("blue", "rabbit", "fish", "blue", "river"))
        assertEquals(hashMapOf(Pair("blue", Pair(0.4f, listOf(0,3))),
            Pair("rabbit", Pair(0.2f, listOf(1))),
            Pair("fish", Pair(0.2f, listOf(2))),
            Pair("river", Pair(0.2f, listOf(4)))), dict)
    }

}