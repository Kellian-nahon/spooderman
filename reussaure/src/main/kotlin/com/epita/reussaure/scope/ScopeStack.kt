package com.epita.reussaure.scope

import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.annotation.Pure
import com.epita.reussaure.exception.InvalidScopePopException
import java.util.*


interface ScopeStack {
    companion object {
        private val MIN_STACK_SIZE = 1
    }

    @Pure
    fun getScopeStack(): Deque<Scope>

    @Pure
    fun getHead(): Scope {
        return getScopeStack().peek()
    }

    @Mutate
    fun pushScope(scope: Scope) {
        pushScope(scope) {}
    }

    @Mutate
    fun pushScope(init: Scope.() -> Unit) {
        pushScope(DefaultScope(), init)
    }

    @Mutate
    fun pushScope(scope: Scope, init: Scope.() -> Unit) {
        init(scope)
        getScopeStack().push(scope)
    }

    @Mutate
    fun popScope(): Scope {
        if (getScopeStack().size <= MIN_STACK_SIZE) {
            throw InvalidScopePopException()
        }
        return getScopeStack().pop()
    }
}