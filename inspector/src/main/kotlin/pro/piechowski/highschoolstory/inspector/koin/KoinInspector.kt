package pro.piechowski.highschoolstory.inspector.koin

import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.instance.SingleInstanceFactory
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@KoinInternalApi
class KoinInspector : KoinComponent {
    private val model = KoinInspectorModel()
    private val viewModel = KoinInspectorViewModel(model)
    private val view = KoinInspectorView(viewModel)

    private val stage =
        Stage().apply {
            scene = view.scene
            title = "Koin"
            x = 0.0
            y = 0.0
        }

    fun show() = stage.show()
}
