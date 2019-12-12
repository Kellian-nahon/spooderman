package com.epita.reussaure.exception

class ProxyTypeNotAnInterfaceException(
        name: String)
    : Exception(String.format(ExceptionMessage.PROXY_TYPE_NOT_AN_INTERFACE.message, name))