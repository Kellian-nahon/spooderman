package com.epita.reussaure.scope

import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.annotation.Pure
import com.epita.reussaure.provider.Provider


interface Scope {
    @NotNull
    @Mutate
    fun <BEAN_TYPE : Any> addProvider(@NotNull provider: Provider<BEAN_TYPE>, init: Provider<BEAN_TYPE>.() -> Unit = {})

    @NotNull
    @Pure
    fun <BEAN_TYPE : Any> getProvider(@NotNull providerClass: Class<BEAN_TYPE>): Provider<BEAN_TYPE>
}