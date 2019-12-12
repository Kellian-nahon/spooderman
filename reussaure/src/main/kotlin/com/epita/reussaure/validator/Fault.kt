package com.epita.reussaure.validator

import com.epita.reussaure.exception.InvalidArgumentException

enum class Fault(
        val message: String,
        val predicate: (Any?) -> Boolean) {

    NULL("is null", Predicate.isNull);

    fun forField(field: String): InvalidArgumentException {
        return InvalidArgumentException(field, this)
    }

    fun <OBJECT_TYPE> validate(value: OBJECT_TYPE, field: String) {
        if (predicate(value)) {
            throw forField(field)
        }
    }

    fun <OBJECT_TYPE> validate(vararg valueFields: Pair<OBJECT_TYPE, String>) {
        valueFields.forEach {
            validate(it.first, it.second)
        }
    }
}