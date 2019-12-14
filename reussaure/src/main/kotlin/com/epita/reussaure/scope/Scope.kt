package com.epita.reussaure.scope

import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.annotation.Pure
import com.epita.reussaure.provider.Provider


interface Scope {
    @Mutate
    fun <BEAN_TYPE : Any> addProvider(provider: Provider<BEAN_TYPE>, init: Provider<BEAN_TYPE>.() -> Unit = {})

    @Pure
    fun <BEAN_TYPE : Any> getProvider(providerClass: Class<BEAN_TYPE>): Provider<BEAN_TYPE>
}