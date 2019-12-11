package com.epita.spooderman.utils

class MutableMultiMap<KEY_TYPE, ELEMENT_TYPE>(private val impl: MutableMap<KEY_TYPE, MutableList<ELEMENT_TYPE>>): MutableMap<KEY_TYPE, MutableList<ELEMENT_TYPE>> by impl {
    fun addValue(key: KEY_TYPE, element: ELEMENT_TYPE) {
        if (get(key) == null) {
            this[key] = mutableListOf()
        }
        get(key)!!.add(element)
    }
}

fun <K, V> mutableMultiMapOf(): MutableMultiMap<K, V> =
    MutableMultiMap(mutableMapOf())

fun <K, V> mutableMultiMapOf(vararg pairs: Pair<K, MutableList<V>>): MutableMultiMap<K, V> =
    MutableMultiMap(mutableMapOf(*pairs))



