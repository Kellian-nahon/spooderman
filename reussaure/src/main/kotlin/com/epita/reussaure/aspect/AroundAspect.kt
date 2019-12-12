package com.epita.reussaure.aspect

import com.epita.reussaure.annotation.Mutate
import com.epita.reussaure.provider.Provider
import com.epita.reussaure.validator.Fault
import org.jetbrains.annotations.NotNull
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class AroundAspect<BEAN_TYPE : Any>(
        @NotNull targetMethod: Method,
        @NotNull aspect: ProvidingAspectConsumer<BEAN_TYPE>)
    : AbstractAspect<BEAN_TYPE, ProvidingAspectConsumer<BEAN_TYPE>>(targetMethod, aspect) {

    @NotNull
    @Mutate
    override fun proxify(@NotNull provider: Provider<BEAN_TYPE>, @NotNull bean: BEAN_TYPE): BEAN_TYPE {
        Fault.NULL.validate(Pair(provider, "provider"), Pair(bean, "bean"))
        return Proxy.newProxyInstance(
                bean.javaClass.classLoader,
                arrayOf(provider.provideForClass())
        ) { obj: Any, method: Method, args: Array<Any>? ->
            val nonNullArgs = args ?: arrayOf()
            if (checkMethod(method)) {
                aspect({ method.invoke(bean, *nonNullArgs) }, bean, method, nonNullArgs)
            } else {
                method.invoke(bean, *nonNullArgs)
            }
        } as BEAN_TYPE
    }
}