package com.epita.butineur.cleaners

import com.epita.spooderman.annotation.NotNull
import com.epita.spooderman.annotation.Pure
import java.net.URL

interface Cleaner {
    @Pure
    @NotNull
    fun cleanup(@NotNull input: String): String
}