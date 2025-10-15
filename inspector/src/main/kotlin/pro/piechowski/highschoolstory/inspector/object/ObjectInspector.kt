package pro.piechowski.highschoolstory.inspector.`object`

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.WindowEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class ObjectInspector(
    initialObject: Any,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val objects = MutableStateFlow(ArrayDeque(listOf(initialObject)))
    private val currentIndex = MutableStateFlow(0)
    private val currentObject =
        objects
            .combine(currentIndex) { objects, index -> objects[index] }
            .stateIn(coroutineScope, SharingStarted.Eagerly, initialObject)

    private var refreshJob: Job? = null

    private val view = ObjectInspectorView(currentObject)
    private val controller = ObjectInspectorController(this, view)
    private val scene = Scene(view)

    private val properties =
        currentObject
            .map { it::class.memberProperties.mapNotNull { it as? KProperty1<Any, Any?> } }
            .stateIn(
                coroutineScope,
                SharingStarted.Eagerly,
                emptyList(),
            )

    init {
        refreshJob =
            coroutineScope.launch {
                properties.collect { properties ->
                    controller.setProperties(properties)
                }

                while (true) {
                    controller.setProperties(properties.value)
                    delay(2000)
                }
            }

        Stage()
            .apply {
                scene = this@ObjectInspector.scene
                onCloseRequest =
                    EventHandler<WindowEvent> {
                        refreshJob?.cancel()
                        if (instance == this@ObjectInspector) instance = null
                    }
            }.show()
    }

    fun pushObject(item: Any) {
        objects.value = ArrayDeque(objects.value.take(currentIndex.value + 1) + item)
        currentIndex.value++
    }

    companion object {
        private var instance: ObjectInspector? = null

        fun pushObject(`object`: Any) {
            instance?.pushObject(`object`) ?: ObjectInspector(`object`).also { instance = it }
        }
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
