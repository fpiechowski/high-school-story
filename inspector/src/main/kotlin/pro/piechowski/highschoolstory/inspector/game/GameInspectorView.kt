package pro.piechowski.highschoolstory.inspector.game

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import pro.piechowski.highschoolstory.inspector.asObservableValue

class GameInspectorView(
    private val viewModel: GameInspectorViewModel,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val launchButton: Button =
        Button("Launch")
            .apply {
                onAction = viewModel.launchGameButtonEventHandler
                disableProperty().bind(viewModel.gameRunning.asObservableValue(coroutineScope, false))
            }

    private val root = HBox(launchButton)

    val scene = Scene(root)
}
