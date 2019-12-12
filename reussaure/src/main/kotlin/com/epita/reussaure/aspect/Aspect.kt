package com.epita.reussaure.aspect

import com.epita.reussaure.annotation.Mutate
import com.epita.reussaure.annotation.NotNull
import com.epita.reussaure.provider.Provider

interface Aspect<BEAN_TYPE : Any> {
    @NotNull
    @Mutate
    fun proxify(@NotNull provider: Provider<BEAN_TYPE>, @NotNull bean: BEAN_TYPE): BEAN_TYPE
}