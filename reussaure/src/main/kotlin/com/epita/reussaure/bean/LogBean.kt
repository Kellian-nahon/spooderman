package com.epita.reussaure.bean

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface LogBean  {
    fun logger(): Logger {
        return LoggerFactory.getLogger(this::class.java)
    }
}