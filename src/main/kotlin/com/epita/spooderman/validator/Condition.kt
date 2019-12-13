package com.epita.spooderman.validator

import com.epita.spooderman.annotation.Nullable
import com.epita.spooderman.annotation.Pure

enum class Condition(val predicate: (Any?) -> Boolean) {
    IS_NOT_NULL(Predicate.isNotNull);

    @Pure
    fun <OBJECT_TYPE> validate(@Nullable value: OBJECT_TYPE): Boolean {
        return predicate(value)
    }
}