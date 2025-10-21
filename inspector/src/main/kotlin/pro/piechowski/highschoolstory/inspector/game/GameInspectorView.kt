package pro.piechowski.highschoolstory.inspector.game

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pro.piechowski.highschoolstory.inspector.InspectorView
import pro.piechowski.highschoolstory.inspector.SharedInspectorViewModel
import pro.piechowski.highschoolstory.inspector.asObservableValue

class GameInspectorView(
    override val viewModel: GameInspectorViewModel,
    override val sharedInspectorViewModel: SharedInspectorViewModel
): InspectorView<GameInspectorViewModel>() {

    private val launchButton: Button =
        Button("Launch")
            .apply {
                onAction = viewModel.launchGameButtonEventHandler
                disableProperty().bind(viewModel.gameRunning.asObservableValue(coroutineScope, false))
            }

    override val title: String = "Game"

    override fun Stage.stageSetup() {
        minWidth = 300.0
        minHeight = 100.0
    }

    override val root = HBox(launchButton)
}
