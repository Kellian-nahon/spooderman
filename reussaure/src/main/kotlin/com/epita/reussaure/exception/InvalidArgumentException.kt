package com.epita.reussaure.exception

import com.epita.reussaure.validator.Fault

class InvalidArgumentException(
        field: String,
        val fault: Fault
) : Exception(String.format(ExceptionMessage.INVALID_ARGUMENT.message, field, fault.message))