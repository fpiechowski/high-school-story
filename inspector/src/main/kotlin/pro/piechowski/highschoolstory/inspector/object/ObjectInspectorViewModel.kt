package pro.piechowski.highschoolstory.inspector.`object`

import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.collections.FXCollections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import pro.piechowski.highschoolstory.inspector.container.Object
import pro.piechowski.highschoolstory.inspector.tickerFlow
import pro.piechowski.highschoolstory.inspector.tryGetPropertyValue
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
class ObjectInspectorViewModel(
    initialObject: Object<Any>?,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val logger = KotlinLogging.logger { }

    private val objects =
        MutableStateFlow(ArrayDeque(initialObject?.let { listOf(it) } ?: emptyList()))

    private val currentIndex =
        MutableStateFlow<Int?>(null)

    val currentObject =
        objects
            .combine(currentIndex) { objects, index -> index?.let { objects[it] } }
            .stateIn(coroutineScope, SharingStarted.Eagerly, initialObject)

    val properties =
        currentObject
            .flatMapLatest { currentObject ->
                currentObject?.let { currentObject ->
                    tickerFlow(2.seconds).map {
                        currentObject.properties
                    }
                } ?: flowOf(FXCollections.emptyObservableList())
            }.map { FXCollections.observableList(it) }

    fun navigateBack() {
        currentIndex.update { currentIndex ->
            currentIndex?.let { currentIndex ->
                if (currentIndex > 0) {
                    currentIndex - 1
                } else {
                    currentIndex
                }
            }
        }
    }

    fun navigateForward() {
        currentIndex.update { currentIndex ->
            currentIndex?.let { currentIndex ->
                if (currentIndex < objects.value.size - 1) {
                    currentIndex + 1
                } else {
                    currentIndex
                }
            }
        }
    }

    fun pushObject(item: Object<Any>) {
        objects.update { objects ->
            currentIndex.value?.let { currentIndex ->
                ArrayDeque(objects.take(currentIndex + 1) + item)
            } ?: ArrayDeque(listOf(item))
        }

        currentIndex.update { index ->
            index?.let { it + 1 } ?: 0
        }
    }

    private val Object<*>.properties
        get() =
            type
                .memberProperties
                .map {
                    ObjectProperty(
                        it.name,
                        try {
                            this.instance?.tryGetPropertyValue<Any>(it as KProperty1<Any, Any?>)
                        } catch (_: Throwable) {
                            "error"
                        },
                    )
                }.let { objectProperties ->
                    objectProperties + (instance?.mapElementsToObjectProperties() ?: emptyList())
                }

    private fun Any.mapElementsToObjectProperties() =
        when (this) {
            is Iterable<*> ->
                mapIndexed { idx, value -> ObjectProperty(idx.toString(), value) }

            is Array<*> ->
                mapIndexed { idx, value -> ObjectProperty(idx.toString(), value) }

            else -> listOf()
        }

    data class ObjectProperty(
        val name: String,
        val value: Any?,
    )
}
