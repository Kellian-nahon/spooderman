package com.epita.reussaure.aspect

import com.epita.spooderman.annotation.Pure
import org.jetbrains.annotations.NotNull
import java.lang.reflect.Method

abstract class AbstractAspect<BEAN_TYPE : Any, ASPECT_TYPE>(
        @NotNull val targetMethod: Method,
        @NotNull val aspect: ASPECT_TYPE) : Aspect<BEAN_TYPE> {

    @Pure
    @NotNull
    protected fun checkMethod(@NotNull invokedMethod: Method): Boolean {
        return targetMethod.name == invokedMethod.name
    }
}