package pro.piechowski.highschoolstory.inspector.game

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.HBox

class GameInspectorView {
    private val launchButton: Button = Button("Launch")
    private val root = HBox(launchButton)

    val scene = Scene(root)
}
