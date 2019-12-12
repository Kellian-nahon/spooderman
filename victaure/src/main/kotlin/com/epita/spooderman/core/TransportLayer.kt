package com.epita.spooderman.core

import com.epita.spooderman.cleaners.Cleaner
import com.epita.spooderman.transport.Transporter

class TransportLayer(private val transporter: Transporter, private val cleaner: Cleaner){
    fun getDocument(): DocumentContent {
        return cleaner.cleanup(transporter())
    }
}