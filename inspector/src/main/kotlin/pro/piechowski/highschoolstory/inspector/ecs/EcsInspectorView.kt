package pro.piechowski.highschoolstory.inspector.ecs

import com.github.quillraven.fleks.Component
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Scene
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.VBox
import javafx.util.Callback

class EcsInspectorView(
    private val viewModel: EcsInspectorViewModel,
) {
    private val componentNameColumn =
        TableColumn<Component<Any>, String>()
            .apply {
                text = "Name"
                cellValueFactory =
                    Callback {
                        SimpleStringProperty(it.value::class.simpleName)
                    }
            }

    private val componentTable =
        TableView<Component<Any>>().apply {
            columns += componentNameColumn
            itemsProperty().bind()
        }

    private val root =
        VBox().apply {
            children += componentTable
        }

    val scene = Scene(root)
}
