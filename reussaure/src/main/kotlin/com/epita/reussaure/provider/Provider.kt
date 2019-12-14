package com.epita.reussaure.provider

import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.annotation.Pure
import com.epita.reussaure.aspect.*
import com.epita.reussaure.exception.ProxyTypeNotAnInterfaceException
import java.lang.reflect.Method

interface Provider<BEAN_TYPE : Any> {
    val aspectList: ArrayList<Aspect<BEAN_TYPE>>

    @Mutate
    fun before(method: Method?, block: AspectConsumer<BEAN_TYPE>) {
        if (method != null) {
            aspectList.add(BeforeAspect(method as Method, block))
        }
    }

    @Mutate
    fun around(method: Method?, block: ProvidingAspectConsumer<BEAN_TYPE>) {
        if (method != null) {
            aspectList.add(AroundAspect(method as Method, block))
        }
    }

    @Mutate
    fun after(method: Method?, block: AspectConsumer<BEAN_TYPE>) {
        if (method != null) {
            aspectList.add(AfterAspect(method as Method, block))
        }
    }

    @Pure
    fun provide(): BEAN_TYPE

    @Pure
    fun provideForClass(): Class<BEAN_TYPE>

    @Mutate
    fun proxify(provider: Provider<BEAN_TYPE>, bean: BEAN_TYPE): BEAN_TYPE {
        if (aspectList.isEmpty()) {
            return bean
        }

        val provideForClass = provideForClass()
        if (!provideForClass.isInterface) {
            throw ProxyTypeNotAnInterfaceException("provideForClass")
        }

        var proxied = bean

        aspectList.forEach { aspect -> proxied = aspect.proxify(this, proxied) }

        return proxied
    }
}