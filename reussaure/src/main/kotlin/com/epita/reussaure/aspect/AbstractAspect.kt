package com.epita.reussaure.aspect

import com.epita.reussaure.annotation.Pure
import com.epita.reussaure.validator.Fault
import org.jetbrains.annotations.NotNull
import java.lang.reflect.Method

abstract class AbstractAspect<BEAN_TYPE : Any, ASPECT_TYPE>(
        @NotNull val targetMethod: Method,
        @NotNull val aspect: ASPECT_TYPE) : Aspect<BEAN_TYPE> {

    @Pure
    @NotNull
    protected fun checkMethod(@NotNull invokedMethod: Method): Boolean {
        Fault.NULL.validate(Pair(invokedMethod, "invokedMethod"), Pair(targetMethod, "targetMethod"))
        return targetMethod.name == invokedMethod.name
    }
}