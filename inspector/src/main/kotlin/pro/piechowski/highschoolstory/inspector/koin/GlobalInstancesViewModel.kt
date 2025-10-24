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
import pro.piechowski.highschoolstory.inspector.InspectorViewModel
import pro.piechowski.highschoolstory.inspector.globals.GlobalInstance
import pro.piechowski.highschoolstory.inspector.globals.GlobalInstances

@KoinInternalApi
class GlobalInstancesViewModel(
    private val globalInstances: GlobalInstances,
) : InspectorViewModel() {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val instances =
        MutableStateFlow<ObservableList<GlobalInstance<Any>>>(
            FXCollections.observableList(
                emptyList(),
            ),
        )

    init {
        coroutineScope.launch {
            while (true) {
                instances.value = FXCollections.observableList(globalInstances.instances)
                delay(2000)
            }
        }
    }
}
