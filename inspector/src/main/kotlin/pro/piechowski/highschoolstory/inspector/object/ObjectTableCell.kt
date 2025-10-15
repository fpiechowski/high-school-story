package pro.piechowski.highschoolstory.inspector.`object`

import javafx.event.EventHandler
import javafx.scene.control.TableCell
import javafx.scene.input.MouseButton

class ObjectTableCell<T : Any, S : Any?> : TableCell<T, S>() {
    override fun updateItem(
        item: S?,
        empty: Boolean,
    ) {
        super.updateItem(item, empty)
        text = if (empty) null else item.toString()
    }

    init {
        onMouseClicked =
            EventHandler { event ->
                item?.let { item ->
                    if (item::class.javaPrimitiveType == null) {
                        when {
                            event.button == MouseButton.PRIMARY && event.clickCount == 2 && !isEmpty ->
                                ObjectInspector.pushObject(item)
                            event.button == MouseButton.MIDDLE ->
                                ObjectInspector(item)
                        }
                    }
                }
            }
    }
}
