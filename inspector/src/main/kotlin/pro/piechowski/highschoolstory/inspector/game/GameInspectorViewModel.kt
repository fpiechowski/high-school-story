package pro.piechowski.highschoolstory.inspector.game

import javafx.event.ActionEvent
import javafx.event.EventHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pro.piechowski.highschoolstory.inspector.InspectorViewModel

class GameInspectorViewModel(
    private val model: GameInspectorModel,
) : InspectorViewModel() {
    val gameRunning: Flow<Boolean> = model.gameLaunchJob.map { it != null && it.isActive }

    val launchGameButtonEventHandler =
        EventHandler<ActionEvent> {
            model.launchGame()
        }
}
