package pro.piechowski.highschoolstory.inspector.game

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.stage.WindowEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class GameInspector(
    gameScope: CoroutineScope,
) {
    private val model = GameInspectorModel(gameScope)
    private val viewModel = GameInspectorViewModel(model)
    private val view = GameInspectorView(viewModel)
    private val stage =
        Stage().apply {
            minWidth = 300.0
            minHeight = 100.0
            scene = view.scene
            title = "Game"
            centerOnScreen()
            y = 0.0
            onCloseRequest =
                EventHandler<WindowEvent> {
                    Platform.exit()
                }
        }

    fun show() {
        stage.show()
    }
}
