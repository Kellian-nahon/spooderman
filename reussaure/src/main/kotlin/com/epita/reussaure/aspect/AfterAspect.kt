package com.epita.reussaure.aspect

import com.epita.reussaure.annotation.Mutate
import com.epita.reussaure.annotation.NotNull
import com.epita.reussaure.provider.Provider
import com.epita.reussaure.validator.Fault
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class AfterAspect<BEAN_TYPE : Any>(
        @NotNull targetMethod: Method,
        @NotNull aspect: AspectConsumer<BEAN_TYPE>)
    : AbstractAspect<BEAN_TYPE, AspectConsumer<BEAN_TYPE>>(targetMethod, aspect) {

    @NotNull
    @Mutate
    override fun proxify(@NotNull provider: Provider<BEAN_TYPE>, @NotNull bean: BEAN_TYPE): BEAN_TYPE {
        Fault.NULL.validate(Pair(provider, "provider"), Pair(bean, "bean"))
        return Proxy.newProxyInstance(
                bean.javaClass.classLoader,
                arrayOf(provider.provideForClass())
        ) { _: Any, method: Method, args: Array<Any>? ->
            val res = method.invoke(bean, *(args ?: arrayOf()))
            if (checkMethod(method)) {
                aspect(bean, method, args ?: arrayOf())
            }
            res
        } as BEAN_TYPE
    }
}
