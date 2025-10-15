package pro.piechowski.highschoolstory.inspector.koin

import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.instance.SingleInstanceFactory
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@KoinInternalApi
class KoinInspectorModel {
    val instances
        get() =
            GlobalContext
                .getOrNull()
                ?.instanceRegistry
                ?.instances
                ?.values
                ?.filterIsInstance<SingleInstanceFactory<*>>()
                ?.map { factory ->
                    val valueProp = factory::class.memberProperties.find { it.name == "value" }
                    valueProp?.isAccessible = true
                    factory to (valueProp as? KProperty1<Any, *>)?.get(factory)
                }?.sortedBy { it.first.beanDefinition.primaryType.simpleName } ?: emptyList()
}
