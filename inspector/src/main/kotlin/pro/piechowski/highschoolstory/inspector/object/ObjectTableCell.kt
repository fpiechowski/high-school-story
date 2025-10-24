package pro.piechowski.highschoolstory.inspector.`object`

import javafx.event.EventHandler
import javafx.scene.control.TableCell
import javafx.scene.input.MouseButton
import pro.piechowski.highschoolstory.inspector.fullTypeName

class ObjectTableCell<T : Any, S : Any?>(
    objectInspectorViewModel: ObjectInspectorViewModel? = null,
) : TableCell<T, S>() {
    override fun updateItem(
        item: S?,
        empty: Boolean,
    ) {
        super.updateItem(item, empty)
        text =
            if (empty) {
                null
            } else {
                item?.let {
                    if (item::class.isData) {
                        "${item::class.fullTypeName}${item.toString().substringAfter("(").let { "($it" }}"
                    } else {
                        "${item::class.fullTypeName}@${Integer.toHexString(item.hashCode())}"
                    }
                }
            }
    }

    init {
        onMouseClicked =
            EventHandler { event ->
                item?.let { item ->
                    if (item::class.javaPrimitiveType == null) {
                        when (event.button) {
                            MouseButton.PRIMARY if event.clickCount == 2 && !isEmpty ->
                                objectInspectorViewModel?.pushObject(item)
                                    ?: TODO("implement opening new object inspector instance")

                            MouseButton.MIDDLE ->
                                TODO("implement opening new object inspector instance")

                            else -> {}
                        }
                    }
                }
            }
    }
}
