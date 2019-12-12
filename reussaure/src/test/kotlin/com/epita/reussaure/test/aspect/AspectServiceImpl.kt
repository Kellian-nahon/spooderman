package com.epita.reussaure.test.aspect

class AspectServiceImpl(
        val valueList: ArrayList<String>
) : AspectService {
    override fun addValue(value: String) {
        valueList.add(value)
    }

    override fun anotherMethod() {
    }
}