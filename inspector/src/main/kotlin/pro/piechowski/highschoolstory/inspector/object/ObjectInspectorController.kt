package pro.piechowski.highschoolstory.inspector.`object`

import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import kotlin.reflect.KProperty1

class ObjectInspectorController(
    private val model: ObjectInspector,
    private val view: ObjectInspectorView,
) {
    fun setProperties(properties: List<KProperty1<Any, Any?>>) {
        view.items.setAll(properties)
    }

    init {
        view.addEventHandler<MouseEvent>(MouseEvent.MOUSE_CLICKED) { event ->
            when (event.button) {
                MouseButton.BACK -> model.navigateBack()
                MouseButton.FORWARD -> model.navigateForward()
                else -> {}
            }
        }
    }
}
