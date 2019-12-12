package com.epita.reussaure.scope

import com.epita.reussaure.annotation.Mutate
import com.epita.reussaure.annotation.NotNull
import com.epita.reussaure.annotation.Pure
import com.epita.reussaure.exception.BeanNotFoundException
import com.epita.reussaure.provider.Provider
import com.epita.reussaure.validator.Fault

open class DefaultScope : Scope {
    private val providers: ArrayList<Provider<Any>> = arrayListOf()

    @Mutate
    override fun <BEAN_TYPE : Any> addProvider(@NotNull provider: Provider<BEAN_TYPE>,
                                               @NotNull init: Provider<BEAN_TYPE>.() -> Unit) {
        Fault.NULL.validate(Pair(provider, "provider"), Pair(init, "init"))
        init(provider)
        providers.add(provider as Provider<Any>)
    }

    @NotNull
    @Pure
    override fun <@NotNull BEAN_TYPE : Any> getProvider(@NotNull providerClass: Class<BEAN_TYPE>): Provider<BEAN_TYPE> {
        Fault.NULL.validate(providerClass, "providerClass")
        return providers
                .filter { provider -> providerClass.isAssignableFrom(provider.provideForClass()) }
                .map { provider -> provider as Provider<BEAN_TYPE> }
                .ifEmpty { throw BeanNotFoundException(providerClass.simpleName) }
                .first()
    }

}
