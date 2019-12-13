package com.epita.spooderman.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class NotNull {
}