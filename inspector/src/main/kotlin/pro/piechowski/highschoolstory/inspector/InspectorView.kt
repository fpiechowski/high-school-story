package pro.piechowski.highschoolstory.inspector

import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

abstract class InspectorView<VM : InspectorViewModel> {
    protected abstract val title: String

    protected val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    protected abstract fun Stage.stageSetup()

    protected abstract val sharedInspectorViewModel: SharedInspectorViewModel
    protected abstract val viewModel: VM

    protected abstract val root: Parent

    protected val scene: Scene = Scene(root)

    val stage: Stage = Stage().apply {
        scene = this@InspectorView.scene
        title = this@InspectorView.title

        focusedProperty().addListener { _, _, newValue ->
            coroutineScope.launch { if (newValue) sharedInspectorViewModel.notifyAnyInspectorFocused() }
        }
    }

    init {
        coroutineScope.launch {
            launch {
                sharedInspectorViewModel.toFrontAction.collect {
                    stage.toFront()
                }
            }

            launch {
                viewModel.opened.collect { opened ->
                    stage.apply {
                        if (opened) {
                            show()
                            stageSetup()
                        } else hide()
                    }
                }
            }
        }
    }
}
