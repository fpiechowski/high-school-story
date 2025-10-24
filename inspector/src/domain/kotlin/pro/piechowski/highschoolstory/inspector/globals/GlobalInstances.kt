package pro.piechowski.highschoolstory.inspector.globals

import kotlin.reflect.KClass

data class GlobalInstance<T : Any>(
    val type: KClass<T>,
    val value: T?,
)

interface GlobalInstances {
    val instances: List<GlobalInstance<Any>>
}
