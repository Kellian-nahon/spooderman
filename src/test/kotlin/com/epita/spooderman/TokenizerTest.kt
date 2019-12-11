package com.epita.spooderman

import com.epita.spooderman.tokenisation.Tokenizer
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TokenizerTest {

    @Test
    fun testSynonymeList() {
        val hashMap = Tokenizer().getSynonymHashMap()
        assertNotEquals(null, hashMap)
    }

    @Test
    fun testTokenisation() {
        val tokens = Tokenizer().tokenisation("The blue rabbit is fishing in a blue river.")
        assertEquals(listOf("blue", "rabbit", "fish", "blue", "river"), tokens)
    }

    @Test
    fun testStemming() {
        val words = Tokenizer().applyStemming(listOf("fishing", "tests"))
        assertEquals(listOf("fish", "test"), words)
    }

}
