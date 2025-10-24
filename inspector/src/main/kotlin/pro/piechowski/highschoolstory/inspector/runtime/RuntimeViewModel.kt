package pro.piechowski.highschoolstory.inspector.runtime

import javafx.event.ActionEvent
import javafx.event.EventHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pro.piechowski.highschoolstory.inspector.InspectorViewModel

class RuntimeViewModel(
    private val runtime: Runtime,
) : InspectorViewModel() {
    val gameRunning: Flow<Boolean> = runtime.launchedGameJob.map { it != null && it.isActive }

    val launchGameButtonEventHandler =
        EventHandler<ActionEvent> {
            runtime.launchGame()
        }
}
