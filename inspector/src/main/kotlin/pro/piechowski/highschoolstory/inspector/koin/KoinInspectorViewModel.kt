package pro.piechowski.highschoolstory.inspector.koin

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.instance.SingleInstanceFactory

@KoinInternalApi
class KoinInspectorViewModel(
    private val model: KoinInspectorModel,
) {
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
