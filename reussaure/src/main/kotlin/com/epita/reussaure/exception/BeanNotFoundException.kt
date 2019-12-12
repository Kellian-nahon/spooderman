package com.epita.reussaure.exception

class BeanNotFoundException(name: String) : Exception(String.format(ExceptionMessage.BEAN_NOT_FOUND.message, name))