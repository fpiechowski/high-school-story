package pro.piechowski.highschoolstory.inspector.container

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.koin.core.Koin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.instance.SingleInstanceFactory
import pro.piechowski.highschoolstory.inspector.propertyValue
import pro.piechowski.highschoolstory.inspector.tickerFlow
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
@KoinInternalApi
class KoinObjectContainer : ObjectContainer {
    private val logger = KotlinLogging.logger {}

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val koin: Flow<Koin?> =
        tickerFlow(2.seconds)
            .map { GlobalContext.getOrNull() }
            .distinctUntilChanged()
            .onEach { logger.info { "Koin = $it" } }
            .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override val objects =
        koin.flatMapLatest { koin ->
            tickerFlow(2.seconds)
                .map {
                    koin
                        ?.instanceRegistry
                        ?.instances
                        ?.values
                        ?.filterIsInstance<SingleInstanceFactory<*>>()
                        ?.map { factory ->
                            factory to factory.propertyValue<SingleInstanceFactory<*>, Any?>("value")
                        }?.sortedBy { it.first.beanDefinition.primaryType.simpleName }
                        ?.map { Object(it.first.beanDefinition.primaryType as KClass<Any>, it.second) }
                        ?: emptyList()
                }
        }
}
