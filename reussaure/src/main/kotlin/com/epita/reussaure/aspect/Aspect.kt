package com.epita.reussaure.aspect

import com.epita.spooderman.annotation.Mutate
import com.epita.reussaure.provider.Provider

interface Aspect<BEAN_TYPE : Any> {
    @Mutate
    fun proxify(provider: Provider<BEAN_TYPE>, bean: BEAN_TYPE): BEAN_TYPE
}