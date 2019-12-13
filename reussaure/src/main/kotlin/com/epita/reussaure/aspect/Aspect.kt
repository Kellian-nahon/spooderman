package com.epita.reussaure.aspect

import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.annotation.NotNull
import com.epita.reussaure.provider.Provider

interface Aspect<BEAN_TYPE : Any> {
    @NotNull
    @Mutate
    fun proxify(@NotNull provider: Provider<BEAN_TYPE>, @NotNull bean: BEAN_TYPE): BEAN_TYPE
}