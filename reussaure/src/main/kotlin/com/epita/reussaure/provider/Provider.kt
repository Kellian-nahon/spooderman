package com.epita.reussaure.provider

import com.epita.reussaure.annotation.Mutate
import com.epita.reussaure.annotation.NotNull
import com.epita.reussaure.annotation.Nullable
import com.epita.reussaure.annotation.Pure
import com.epita.reussaure.aspect.*
import com.epita.reussaure.exception.ProxyTypeNotAnInterfaceException
import com.epita.reussaure.validator.Condition
import com.epita.reussaure.validator.Fault
import java.lang.reflect.Method

interface Provider<BEAN_TYPE : Any> {
    val aspectList: ArrayList<Aspect<BEAN_TYPE>>

    @Mutate
    fun before(@Nullable method: Method?, @NotNull block: AspectConsumer<BEAN_TYPE>) {
        Fault.NULL.validate(block, "block")
        if (Condition.IS_NOT_NULL.validate(method)) {
            aspectList.add(BeforeAspect(method as Method, block))
        }
    }

    @Mutate
    fun around(@Nullable method: Method?, @NotNull block: ProvidingAspectConsumer<BEAN_TYPE>) {
        Fault.NULL.validate(block, "block")
        if (Condition.IS_NOT_NULL.validate(method)) {
            aspectList.add(AroundAspect(method as Method, block))
        }
    }

    @Mutate
    fun after(@Nullable method: Method?, @NotNull block: AspectConsumer<BEAN_TYPE>) {
        Fault.NULL.validate(block, "block")
        if (Condition.IS_NOT_NULL.validate(method)) {
            aspectList.add(AfterAspect(method as Method, block))
        }
    }

    @NotNull
    @Pure
    fun provide(): BEAN_TYPE

    @NotNull
    @Pure
    fun provideForClass(): Class<BEAN_TYPE>

    @NotNull
    @Mutate
    fun proxify(@NotNull provider: Provider<BEAN_TYPE>, @NotNull bean: BEAN_TYPE): BEAN_TYPE {
        Fault.NULL.validate(Pair(provider, "provider"), Pair(bean, "bean"))

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