package com.epita.reussaure.provider

import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.annotation.Mutate
import java.util.function.Supplier

class Singleton<BEAN_TYPE : Any, SUPPLIER_BEAN_TYPE : BEAN_TYPE>(provideClass: Class<BEAN_TYPE>,
                                                                 initializer: Supplier<SUPPLIER_BEAN_TYPE>)
    : AbstractProvider<BEAN_TYPE, SUPPLIER_BEAN_TYPE>(provideClass, initializer), LogBean {

    private var value: BEAN_TYPE? = null
    private var isInitialized = false

    @Mutate
    override fun provide(): BEAN_TYPE {
        if (!isInitialized) {
            value = proxify(this, initializer.get())
            isInitialized = true
            if (value == null) {
                logger().warn("Initalized singleton of type ${provideForClass().simpleName} to null")
            }
            else {
                logger().trace("Initalized singleton of type ${provideForClass().simpleName}")
            }
        }
        logger().trace("Providing singleton of type: ${provideForClass().simpleName}")
        return value!!
    }

}