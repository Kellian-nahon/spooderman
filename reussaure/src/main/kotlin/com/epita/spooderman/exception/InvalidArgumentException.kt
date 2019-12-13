package com.epita.spooderman.exception

import com.epita.spooderman.validator.Fault

class InvalidArgumentException(
        field: String,
        val fault: Fault
) : Exception(String.format(ExceptionMessage.INVALID_ARGUMENT.message, field, fault.message))