package pro.piechowski.highschoolstory.inspector.`object`

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.compareTo
import kotlin.dec
import kotlin.inc
import kotlin.plus
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.text.compareTo

class ObjectInspectorModel(
    initialObject: Any,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val objects = MutableStateFlow(ArrayDeque(listOf(initialObject)))
    private val currentIndex = MutableStateFlow(0)
    private val currentObject =
        objects
            .combine(currentIndex) { objects, index -> objects[index] }
            .stateIn(coroutineScope, SharingStarted.Eagerly, initialObject)

     val properties =
        currentObject
            .map { it::class.memberProperties.mapNotNull { it as? KProperty1<Any, Any?> } }
            .stateIn(
                coroutineScope,
                SharingStarted.Eagerly,
                emptyList(),
            )

    fun pushObject(item: Any) {
        objects.value = ArrayDeque(objects.value.take(currentIndex.value + 1) + item)
        currentIndex.value++
    }

    fun navigateBack() {
        if (currentIndex.value > 0) {
            currentIndex.value--
        }
    }

    fun navigateForward() {
        if (currentIndex.value < objects.value.size - 1) {
            currentIndex.value++
        }
    }
}
