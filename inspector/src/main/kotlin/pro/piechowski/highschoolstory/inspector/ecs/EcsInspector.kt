package pro.piechowski.highschoolstory.inspector.ecs

import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.World
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class EcsInspector(
    private val gameScope: CoroutineScope,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val model = EcsInspectorModel()
    private val viewModel = EcsInspectorViewModel(model)
    private val view = EcsInspectorView(viewModel)

    private val stage =
        Stage().apply {
            scene = view.scene
            show()
        }

    init {

        coroutineScope.launch {
            while (true) {
                // TODO(refresh families)
                delay(2000)
            }
        }
    }
}
