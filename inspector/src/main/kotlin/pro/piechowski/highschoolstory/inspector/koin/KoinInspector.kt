package pro.piechowski.highschoolstory.inspector.koin

import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.javafx.asFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.instance.SingleInstanceFactory
import pro.piechowski.highschoolstory.inspector.SharedInspectorViewModel
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@KoinInternalApi
class KoinInspector(
    sharedInspectorViewModel: SharedInspectorViewModel
) : KoinComponent {
    val model = KoinInspectorModel()
    val viewModel = KoinInspectorViewModel(model)
    private val view = KoinInspectorView(viewModel, sharedInspectorViewModel)
}
