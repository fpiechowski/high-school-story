package pro.piechowski.highschoolstory.inspector.koin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.Koin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.instance.SingleInstanceFactory
import pro.piechowski.highschoolstory.inspector.globals.GlobalInstance
import pro.piechowski.highschoolstory.inspector.globals.GlobalInstances
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@ExperimentalCoroutinesApi
@KoinInternalApi
class KoinGlobalInstances : GlobalInstances {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val koin: StateFlow<Koin?> =
        flow {
            while (true) {
                GlobalContext.getOrNull()?.let {
                    emit(it)
                }
                delay(2000)
            }
        }.stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override val instances
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
                }?.sortedBy { it.first.beanDefinition.primaryType.simpleName }
                ?.map { GlobalInstance(it.first.beanDefinition.primaryType as KClass<Any>, it.second) }
                ?: emptyList()
}
