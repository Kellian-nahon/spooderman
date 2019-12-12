package com.epita.victaure.core

import com.epita.victaure.cleaners.Cleaner
import com.epita.victaure.transport.Transporter

class TransportLayer(private val transporter: Transporter, private val cleaner: Cleaner){
    fun getDocument(): DocumentContent {
        return cleaner.cleanup(transporter())
    }
}