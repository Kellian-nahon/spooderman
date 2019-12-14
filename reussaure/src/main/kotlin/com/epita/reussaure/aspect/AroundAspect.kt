package com.epita.reussaure.aspect

import com.epita.spooderman.annotation.Mutate
import com.epita.reussaure.provider.Provider
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class AroundAspect<BEAN_TYPE : Any>(
        targetMethod: Method,
        aspect: ProvidingAspectConsumer<BEAN_TYPE>)
    : AbstractAspect<BEAN_TYPE, ProvidingAspectConsumer<BEAN_TYPE>>(targetMethod, aspect) {

    @Suppress("UNCHECKED_CAST")
    @Mutate
    override fun proxify(provider: Provider<BEAN_TYPE>, bean: BEAN_TYPE): BEAN_TYPE {
        return Proxy.newProxyInstance(
                bean.javaClass.classLoader,
                arrayOf(provider.provideForClass())
        ) { _: Any, method: Method, args: Array<Any>? ->
            val nonNullArgs = args ?: arrayOf()
            if (checkMethod(method)) {
                aspect({ method.invoke(bean, *nonNullArgs) }, bean, method, nonNullArgs)
            } else {
                method.invoke(bean, *nonNullArgs)
            }
        } as BEAN_TYPE
    }
}