package com.epita.reussaure.core

import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.annotation.Pure
import com.epita.reussaure.provider.Provider
import com.epita.reussaure.scope.DefaultScope
import com.epita.reussaure.scope.Scope
import com.epita.reussaure.scope.ScopeStack
import java.util.*


class Reussaure(init: Reussaure.() -> Unit = {}) : ScopeStack, Scope {

    private val scopeDeque: Deque<Scope> = ArrayDeque()

    init {
        this.pushScope(DefaultScope())
        init.invoke(this)
    }

    @Pure
    fun <BEAN_TYPE : Any> instanceOf(expectedClass: Class<BEAN_TYPE>): BEAN_TYPE {
        return getProvider(expectedClass).provide()
    }

    @Pure
    override fun getScopeStack(): Deque<Scope> {
        return scopeDeque
    }

    @Mutate
    override fun <BEAN_TYPE : Any> addProvider(provider: Provider<BEAN_TYPE>,
                                               init: Provider<BEAN_TYPE>.() -> Unit) {
        getHead().addProvider(provider, init)
    }

    @Pure
    override fun <BEAN_TYPE : Any> getProvider(providerClass: Class<BEAN_TYPE>): Provider<BEAN_TYPE> {
        return getHead().getProvider(providerClass)
    }
}