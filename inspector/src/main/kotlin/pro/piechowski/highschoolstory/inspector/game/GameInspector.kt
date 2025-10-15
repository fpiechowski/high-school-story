package pro.piechowski.highschoolstory.inspector.game

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.stage.WindowEvent
import kotlinx.coroutines.Job

class GameInspector {
    private val model = GameInspectorModel()
    private val view = GameInspectorView()
    private val viewModel = GameInspectorViewModel(view, model)
    private val stage =
        Stage().apply {
            minWidth = 300.0
            minHeight = 100.0
            scene = this@GameInspector.scene
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

    fun setGameLaunchJob(job: Job?) {
        _gameLaunchJob.value = job
    }
}
