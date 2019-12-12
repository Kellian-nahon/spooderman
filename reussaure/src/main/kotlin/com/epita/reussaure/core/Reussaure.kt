package com.epita.reussaure.core

import com.epita.reussaure.annotation.Mutate
import com.epita.reussaure.annotation.NotNull
import com.epita.reussaure.annotation.Pure
import com.epita.reussaure.provider.Provider
import com.epita.reussaure.scope.DefaultScope
import com.epita.reussaure.scope.Scope
import com.epita.reussaure.scope.ScopeStack
import com.epita.reussaure.validator.Fault
import java.util.*


class Reussaure(init: Reussaure.() -> Unit = {}) : ScopeStack, Scope {

    private val scopeDeque: Deque<Scope> = ArrayDeque()

    init {
        this.pushScope(DefaultScope())
        init.invoke(this)
    }

    @NotNull
    @Pure
    fun <BEAN_TYPE : Any> instanceOf(@NotNull expectedClass: Class<BEAN_TYPE>): BEAN_TYPE {
        Fault.NULL.validate(expectedClass, "expectedClass")
        return getProvider(expectedClass).provide()
    }

    @NotNull
    @Pure
    override fun getScopeStack(): Deque<Scope> {
        return scopeDeque
    }

    @Mutate
    override fun <BEAN_TYPE : Any> addProvider(@NotNull provider: Provider<BEAN_TYPE>,
                                               @NotNull init: Provider<BEAN_TYPE>.() -> Unit) {
        Fault.NULL.validate(Pair(provider, "provider"), Pair(init, "init"))
        getHead().addProvider(provider, init)
    }

    @Pure
    @NotNull
    override fun <BEAN_TYPE : Any> getProvider(@NotNull providerClass: Class<BEAN_TYPE>): Provider<BEAN_TYPE> {
        Fault.NULL.validate(providerClass, "providerClass")
        return getHead().getProvider(providerClass)
    }
}