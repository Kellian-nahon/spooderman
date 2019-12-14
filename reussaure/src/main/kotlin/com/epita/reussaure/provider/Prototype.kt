package com.epita.reussaure.provider

import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.Mutate
import java.util.function.Supplier


class Prototype<BEAN_TYPE : Any, SUPPLIER_BEAN_TYPE : BEAN_TYPE>(provideClass: Class<BEAN_TYPE>,
                                                                 initializer: Supplier<SUPPLIER_BEAN_TYPE>)
    : AbstractProvider<BEAN_TYPE, SUPPLIER_BEAN_TYPE>(provideClass, initializer), LogBean {

    @Mutate
    override fun provide(): BEAN_TYPE {
        logger().trace("Providing prototype of type: ${provideForClass().simpleName}")
        return proxify(this, initializer.get())
    }

}