package pro.piechowski.highschoolstory.inspector

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.typeOf

private val logger = KotlinLogging.logger { }

fun <T : Any, R> T.propertyValue(name: String): R =
    (
        this::class.memberProperties.find {
            it.name == name
        } as KProperty1<T, R>
    ).also { it.isAccessible = true }.get(this)

fun KClass<*>.overridesToString() = java.overridesToString()

fun Class<*>.overridesToString(): Boolean =
    try {
        val method = getMethod("toString")
        method.declaringClass != Any::class.java
    } catch (_: NoSuchMethodException) {
        false
    }

fun <T> T.tryGetPropertyValue(property: KProperty1<T, Any?>): Any? =
    try {
        if (this is Array<*> && property.name == "size") {
            size
        } else {
            property.isAccessible = true

            when {
                property.returnType.isSubtypeOf(typeOf<StateFlow<*>>()) -> {
                    (property.get(this) as StateFlow<*>).value
                }

                else -> property.get(this)
            }
        }
    } catch (e: Throwable) {
        logger.error(e) { "Error while getting value for property $property of object $this" }
        throw e
    }
