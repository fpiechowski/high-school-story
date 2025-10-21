package pro.piechowski.highschoolstory.inspector.ecs

import javafx.stage.Screen
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.javafx.asFlow
import org.koin.core.Koin
import pro.piechowski.highschoolstory.inspector.SharedInspectorViewModel

@ExperimentalCoroutinesApi
class EcsInspector(
    koin: StateFlow<Koin?>,
    sharedInspectorViewModel: SharedInspectorViewModel
) {
    val model = EcsInspectorModel(koin)
    val viewModel = EcsInspectorViewModel(model)
    private val view = EcsInspectorView(viewModel, sharedInspectorViewModel)
}
