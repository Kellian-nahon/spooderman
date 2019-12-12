package com.epita.reussaure.provider

import com.epita.reussaure.annotation.NotNull
import com.epita.reussaure.annotation.Pure
import com.epita.reussaure.aspect.Aspect
import java.util.function.Supplier

abstract class AbstractProvider<BEAN_TYPE : Any, SUPPLIER_BEAN_TYPE : BEAN_TYPE>(
        private val provideClass: Class<BEAN_TYPE>,
        protected val initializer: Supplier<SUPPLIER_BEAN_TYPE>
) : Provider<BEAN_TYPE> {
    override val aspectList: ArrayList<Aspect<BEAN_TYPE>> = arrayListOf()

    @Pure
    @NotNull
    override fun provideForClass(): Class<BEAN_TYPE> {
        return provideClass
    }

}