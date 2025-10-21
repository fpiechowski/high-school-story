package pro.piechowski.highschoolstory.inspector.koin

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.instance.SingleInstanceFactory
import pro.piechowski.highschoolstory.inspector.InspectorViewModel

@KoinInternalApi
class KoinInspectorViewModel(
    private val model: KoinInspectorModel,
): InspectorViewModel() {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val instances =
        MutableStateFlow<ObservableList<Pair<SingleInstanceFactory<*>, Any?>>>(
            FXCollections.observableList(
                emptyList(),
            ),
        )

    init {
        coroutineScope.launch {
            while (true) {
                instances.value = FXCollections.observableList(model.instances)
                delay(2000)
            }
        }
    }
}
