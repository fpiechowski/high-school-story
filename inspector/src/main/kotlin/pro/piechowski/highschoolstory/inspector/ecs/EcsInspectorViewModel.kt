package pro.piechowski.highschoolstory.inspector.ecs

import com.github.quillraven.fleks.ComponentType
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pro.piechowski.highschoolstory.inspector.InspectorViewModel

@ExperimentalCoroutinesApi
class EcsInspectorViewModel(
    private val model: EcsInspectorModel,
): InspectorViewModel() {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val componentTypes =
        MutableStateFlow<ObservableList<Class<ComponentType<Any>>>>(FXCollections.observableList(emptyList()))
    val componentTypeFilter = MutableStateFlow<Class<ComponentType<Any>>?>(null)
    val components =
        componentTypeFilter
            .combine(model.entityComponents()) { componentType, entityComponents ->
                componentType?.let { componentType ->
                    entityComponents.filter { it.second.type()::class.java == componentType }
                } ?: emptyList()
            }.map { FXCollections.observableList(it) }

    init {
        coroutineScope.launch {
            while (true) {
                componentTypes.value = FXCollections.observableList(model.componentTypes)
            }
        }
    }
}
