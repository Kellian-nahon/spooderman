package com.epita.reussaure

import com.epita.reussaure.core.Reussaure
import com.epita.reussaure.exception.BeanNotFoundException
import com.epita.reussaure.exception.ProxyTypeNotAnInterfaceException
import com.epita.reussaure.provider.Prototype
import com.epita.reussaure.provider.Singleton
import com.epita.reussaure.test.Nested
import com.epita.reussaure.test.TestService
import com.epita.reussaure.test.TestServiceBlipImpl
import com.epita.reussaure.test.TestServiceImpl
import com.epita.reussaure.test.aspect.AspectService
import com.epita.reussaure.test.aspect.AspectServiceImpl
import org.junit.Test
import java.lang.reflect.Method
import java.util.function.Supplier
import kotlin.reflect.jvm.javaMethod
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class BasicReussaureTest {

    @Test
    fun TestSimpleRessaure() {
        val reussaure = Reussaure {
            addProvider(Prototype(TestServiceBlipImpl::class.java, Supplier { TestServiceBlipImpl() }))
            addProvider(Prototype(TestServiceImpl::class.java, Supplier { TestServiceImpl() }))
        }

        assertEquals(reussaure.instanceOf(TestServiceBlipImpl::class.java).pong(), "blip")
        assertEquals(reussaure.instanceOf(TestServiceImpl::class.java).pong(), "pong")
    }

    @Test
    fun TestInstanceOfInterface() {
        val reussaure = Reussaure {
            addProvider(Prototype(TestService::class.java, Supplier { TestServiceImpl() }))
        }

        assert(reussaure.instanceOf(TestService::class.java) is TestServiceImpl)
    }

    @Test
    fun TestBeforeAspect() {

        val valueList = ArrayList<String>()
        val reussaure = Reussaure {
            addProvider(Prototype(AspectService::class.java, Supplier { AspectServiceImpl(valueList) })
            ) {
                before(AspectService::addValue.javaMethod) { _, _, _ ->
                    valueList.add("before")
                }
            }
        }
        reussaure.instanceOf(AspectService::class.java).addValue("method")

        assertEquals(valueList.count(), 2)
        assertEquals(valueList[0], "before")
        assertEquals(valueList[1], "method")
    }

    @Test
    fun TestBeforeAspectIncorrectMethod() {
        val valueList = ArrayList<String>()
        val reussaure = Reussaure {
            addProvider(Prototype(AspectService::class.java, Supplier { AspectServiceImpl(valueList) })
            ) {
                before(AspectService::addValue.javaMethod) { _, _, _ ->
                    valueList.add("before")
                }
            }
        }
        reussaure.instanceOf(AspectService::class.java).anotherMethod()

        assertEquals(valueList.count(), 0)
    }

    @Test
    fun TestAfterAspect() {
        val valueList = ArrayList<String>()
        val reussaure = Reussaure {
            addProvider(Prototype(AspectService::class.java, Supplier { AspectServiceImpl(valueList) })
            ) {
                after(AspectService::addValue.javaMethod) { _, _, _ ->
                    valueList.add("after")
                }
            }
        }
        reussaure.instanceOf(AspectService::class.java).addValue("method")

        assertEquals(valueList.count(), 2)
        assertEquals(valueList[0], "method")
        assertEquals(valueList[1], "after")
    }

    @Test
    fun TestAfterAspectIncorrectMethod() {
        val valueList = ArrayList<String>()
        val reussaure = Reussaure {
            addProvider(Prototype(AspectService::class.java, Supplier { AspectServiceImpl(valueList) })
            ) {
                after(AspectService::addValue.javaMethod) { _, _, _ ->
                    valueList.add("after")
                }
            }
        }
        reussaure.instanceOf(AspectService::class.java).anotherMethod()

        assertEquals(valueList.count(), 0)
    }

    @Test
    fun TestAroundAspect() {
        val valueList = ArrayList<String>()
        val reussaure = Reussaure {
            addProvider(Prototype(AspectService::class.java, Supplier { AspectServiceImpl(valueList) })
            ) {
                around(AspectService::addValue.javaMethod) { execute: () -> Unit, _: AspectService, _: Method, _: Array<Any> ->
                    valueList.add("before")
                    execute()
                    valueList.add("after")
                }
            }
        }
        reussaure.instanceOf(AspectService::class.java).addValue("method")

        assertEquals(valueList.count(), 3)
        assertEquals(valueList[0], "before")
        assertEquals(valueList[1], "method")
        assertEquals(valueList[2], "after")
    }

    @Test
    fun TestAroundAspectIncorrectMethod() {
        val valueList = ArrayList<String>()
        val reussaure = Reussaure {
            addProvider(Prototype(AspectService::class.java, Supplier { AspectServiceImpl(valueList) })
            ) {
                around(AspectService::addValue.javaMethod) { execute: () -> Unit, _: AspectService, _: Method, _: Array<Any> ->
                    valueList.add("before")
                    execute()
                    valueList.add("after")
                }
            }
        }
        reussaure.instanceOf(AspectService::class.java).anotherMethod()

        assertEquals(valueList.count(), 0)
    }

    @Test
    fun TestSingletonProvider() {
        val reussaure = Reussaure {
            addProvider(Singleton(TestService::class.java, Supplier { TestServiceImpl() }))
        }

        val instance1 = reussaure.instanceOf(TestService::class.java)
        val instance2 = reussaure.instanceOf(TestService::class.java)

        assertEquals(instance1, instance2)
    }

    @Test
    fun TestPrototypeProvider() {
        val reussaure = Reussaure {
            addProvider(Prototype(TestService::class.java, Supplier { TestServiceImpl() }))
        }

        val instance1 = reussaure.instanceOf(TestService::class.java)
        val instance2 = reussaure.instanceOf(TestService::class.java)

        assertNotEquals(instance1, instance2)
    }

    @Test
    fun TestFailBeanNotFound() {

        val ressaure = Reussaure {}
        assertFailsWith<BeanNotFoundException> {
            ressaure.instanceOf(TestService::class.java)
        }
    }

    @Test
    fun TestNestedBean() {
        val reussaure = Reussaure {
            addProvider(Prototype(Nested::class.java, Supplier { Nested(instanceOf(TestService::class.java)) }))
            addProvider(Prototype(TestService::class.java, Supplier { TestServiceImpl() }))
        }

        val testService = reussaure.instanceOf(Nested::class.java).service

        assertNotNull(testService)
        assertEquals(testService.pong(), "pong")
    }

    @Test
    fun TestSingletonScope() {
        val reussaure = Reussaure {
            addProvider(Singleton(TestService::class.java, Supplier { TestServiceImpl() }))
        }
        val s1 = reussaure.instanceOf(TestService::class.java)

        reussaure.pushScope {
            addProvider(Singleton(TestService::class.java, Supplier { TestServiceImpl() }))
        }
        val s2 = reussaure.instanceOf(TestService::class.java)

        reussaure.popScope()
        val s3 = reussaure.instanceOf(TestService::class.java)

        assertNotEquals(s1, s2)
        assertEquals(s1, s3)
    }

    @Test
    fun TestProxyTypeNotAnInterface() {
        assertFailsWith<ProxyTypeNotAnInterfaceException> {
            val reussaure = Reussaure {
                addProvider(Singleton(TestServiceImpl::class.java, Supplier { TestServiceImpl() })) {
                    before(TestServiceImpl::ping.javaMethod) { _, _, _ ->
                        println("before")
                    }
                }
            }
            reussaure.instanceOf(TestServiceImpl::class.java)
        }
    }
}
