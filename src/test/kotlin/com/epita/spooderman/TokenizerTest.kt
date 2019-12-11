package com.epita.spooderman

import com.epita.spooderman.tokenisation.Tokenizer
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TokenizerTest {


    @Test
    fun testTokenisation() {
        val tokens = Tokenizer().tokenisation("The blue rabbit is fishing in a blue river.")
        assertEquals(listOf("blue", "rabbit", "fish", "blue", "river"), tokens)
    }




}
