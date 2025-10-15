package pro.piechowski.highschoolstory.inspector.game

import javafx.event.ActionEvent
import javafx.event.EventHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import pro.piechowski.highschoolstory.inspector.asObservableValue

class GameInspectorViewModel(
    private val model: GameInspectorModel,
) {
    val gameRunning: Flow<Boolean> = model.gameLaunchJob.map { it != null }

    val launchGameButtonEventHandler =
        EventHandler<ActionEvent> {
            model.launchGame()
        }
}
