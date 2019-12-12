package com.epita.reussaure.provider

import com.epita.reussaure.annotation.Mutate
import com.epita.reussaure.annotation.NotNull
import java.util.function.Supplier


class Prototype<BEAN_TYPE : Any, SUPPLIER_BEAN_TYPE : BEAN_TYPE>(provideClass: Class<BEAN_TYPE>,
                                                                 initializer: Supplier<SUPPLIER_BEAN_TYPE>)
    : AbstractProvider<BEAN_TYPE, SUPPLIER_BEAN_TYPE>(provideClass, initializer) {

    @Mutate
    @NotNull
    override fun provide(): BEAN_TYPE {
        return proxify(this, initializer.get())
    }

}