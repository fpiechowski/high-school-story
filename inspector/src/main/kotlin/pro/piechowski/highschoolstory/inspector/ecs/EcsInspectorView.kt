package pro.piechowski.highschoolstory.inspector.ecs

import com.github.quillraven.fleks.Component
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.VBox
import javafx.util.Callback

class EcsInspectorView : VBox() {
    val componentNameColumn =
        TableColumn<Component<Any>, String>()
            .apply {
                text = "Name"
                cellValueFactory =
                    Callback {
                        SimpleStringProperty(it.value::class.simpleName)
                    }
            }

    val componentTable =
        TableView<Component<Any>>().apply {
            columns += componentNameColumn
        }

    init {
        children += componentTable
    }
}
