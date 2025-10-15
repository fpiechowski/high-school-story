package pro.piechowski.highschoolstory.inspector.game

import javafx.event.ActionEvent
import javafx.event.EventHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import pro.piechowski.highschoolstory.inspector.asObservableValue

class GameInspectorViewModel(
    private val view: GameInspectorView,
    private val model: GameInspectorModel,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    val gameLaunchJob: StateFlow<Job?> = model.gameLaunchJob

    init {
        view.launchButton.onAction =
            EventHandler<ActionEvent> {
                launchGame()
            }

        view.launchButton.disableProperty().bind(model.gameLaunchJob.map { it != null }.asObservableValue(coroutineScope, false))
    }
}
