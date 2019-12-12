package com.epita.reussaure.validator

class Predicate {
    companion object {
        val isNull: (Any?) -> Boolean = { any -> any == null }
        val isNotNull: (Any?) -> Boolean = { any -> any != null }
    }
}