package com.epita.reussaure.aspect

import java.lang.reflect.Method

typealias AspectConsumer<BEAN_TYPE> = (BEAN_TYPE, Method, Array<Any>) -> Unit
typealias ProvidingAspectConsumer<BEAN_TYPE> = (() -> Unit, BEAN_TYPE, Method, Array<Any>) -> Unit
