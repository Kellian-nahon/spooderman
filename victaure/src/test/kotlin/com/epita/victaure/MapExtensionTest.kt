package com.epita.victaure

import com.epita.spooderman.utils.mutableMultiMapOf
import org.junit.Test

class MapExtensionTest {
    @Test
    fun testAddOrSet() {
        val mutableMap = mutableMultiMapOf<Int, Int>()
        mutableMap.addValue(1, 2)
        assert(mutableMap[1]?.get(0) == 2)
        mutableMap.addValue(1, 3)
        assert(mutableMap[1]?.get(1) == 3)
        mutableMap[2] = arrayListOf(1)
        mutableMap.addValue(2, 3)
        assert(mutableMap[2]?.get(1) == 3)
    }
}