package pro.piechowski.highschoolstory.inspector.runtime

import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.stage.Stage
import pro.piechowski.highschoolstory.inspector.InspectorView
import pro.piechowski.highschoolstory.inspector.asObservableValue

class RuntimeView(
    viewModel: RuntimeViewModel,
) : InspectorView<RuntimeViewModel>(viewModel) {
    private val launchButton: Button =
        Button("Launch")
            .apply {
                onAction = viewModel.launchGameButtonEventHandler
                disableProperty().bind(viewModel.gameRunning.asObservableValue(coroutineScope, false))
            }

    override val root = HBox(launchButton)
}
