package com.epita.reussaure.scope

import com.epita.reussaure.annotation.Mutate
import com.epita.reussaure.annotation.NotNull
import com.epita.reussaure.annotation.Pure
import com.epita.reussaure.exception.InvalidScopePopException
import com.epita.reussaure.validator.Fault
import java.util.*


interface ScopeStack {
    companion object {
        private val MIN_STACK_SIZE = 1
    }

    @NotNull
    @Pure
    fun getScopeStack(): Deque<Scope>

    @Pure
    @NotNull
    fun getHead(): Scope {
        return getScopeStack().peek()
    }

    @Mutate
    fun pushScope(@NotNull scope: Scope) {
        pushScope(scope) {}
    }

    @Mutate
    fun pushScope(@NotNull init: Scope.() -> Unit) {
        pushScope(DefaultScope(), init)
    }

    @Mutate
    fun pushScope(@NotNull scope: Scope, @NotNull init: Scope.() -> Unit) {
        Fault.NULL.validate(Pair(scope, "scope"), Pair(init, "init"))
        init(scope)
        getScopeStack().push(scope)
    }

    @Mutate
    @NotNull
    fun popScope(): Scope {
        if (getScopeStack().size <= MIN_STACK_SIZE) {
            throw InvalidScopePopException()
        }
        return getScopeStack().pop()
    }
}