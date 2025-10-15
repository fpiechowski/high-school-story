package pro.piechowski.highschoolstory.inspector.ecs

import com.github.quillraven.fleks.Component
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EcsInspectorViewModel(
    private val model: EcsInspectorModel,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val components = MutableStateFlow<ObservableList<Component<Any>>>(FXCollections.observableList(emptyList()))

    init {
        coroutineScope.launch {
            while (true) {
                components.value = FXCollections.observableList(model.components.flatMap { it. })
            }
        }
    }
}
