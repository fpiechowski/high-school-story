package pro.piechowski.highschoolstory.inspector

import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.javafx.JavaFx

abstract class InspectorView<VM : InspectorViewModel>(
    protected val viewModel: VM,
) {
    protected val coroutineScope = CoroutineScope(Dispatchers.JavaFx + SupervisorJob())

    abstract val root: Parent
}
