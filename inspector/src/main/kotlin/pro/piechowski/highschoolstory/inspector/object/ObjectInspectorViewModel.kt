package pro.piechowski.highschoolstory.inspector.`object`

import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import pro.piechowski.highschoolstory.inspector.InspectorViewModel
import kotlin.reflect.KProperty1

class ObjectInspectorViewModel(
    private val model: ObjectInspectorModel,
): InspectorViewModel() {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var refreshJob: Job? = null

    val properties = MutableStateFlow<List<KProperty1<Any, Any?>>>()

    init {
        refreshJob =
            coroutineScope.launch {
                model.properties.collect { properties ->
                    this@ObjectInspectorViewModel.properties.value = properties
                }

                while (true) {
                    this@ObjectInspectorViewModel.properties.value = model.properties.value
                    delay(2000)
                }
            }
    }
}
