package com.epita.reussaure.validator

import com.epita.reussaure.annotation.Nullable
import com.epita.reussaure.annotation.Pure

enum class Condition(val predicate: (Any?) -> Boolean) {
    IS_NOT_NULL(Predicate.isNotNull);

    @Pure
    fun <OBJECT_TYPE> validate(@Nullable value: OBJECT_TYPE): Boolean {
        return predicate(value)
    }
}