package com.epita.reussaure.scope

import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.annotation.Pure
import com.epita.reussaure.exception.BeanNotFoundException
import com.epita.reussaure.provider.Provider

open class DefaultScope : Scope {
    private val providers: ArrayList<Provider<Any>> = arrayListOf()

    @Suppress("UNCHECKED_CAST")
    @Mutate
    override fun <BEAN_TYPE : Any> addProvider(provider: Provider<BEAN_TYPE>,
                                               init: Provider<BEAN_TYPE>.() -> Unit) {
        init(provider)
        providers.add(provider as Provider<Any>)
    }

    @Suppress("UNCHECKED_CAST")
    @Pure
    override fun <BEAN_TYPE : Any> getProvider(providerClass: Class<BEAN_TYPE>): Provider<BEAN_TYPE> {
        return providers
                .filter { provider -> providerClass.isAssignableFrom(provider.provideForClass()) }
                .map { provider -> provider as Provider<BEAN_TYPE> }
                .ifEmpty { throw BeanNotFoundException(providerClass.simpleName) }
                .first()
    }

}
