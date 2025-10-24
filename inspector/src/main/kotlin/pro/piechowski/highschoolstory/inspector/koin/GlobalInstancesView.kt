package pro.piechowski.highschoolstory.inspector.koin

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.layout.Region.USE_COMPUTED_SIZE
import javafx.scene.layout.VBox
import javafx.util.Callback
import org.koin.core.annotation.KoinInternalApi
import pro.piechowski.highschoolstory.inspector.InspectorView
import pro.piechowski.highschoolstory.inspector.asObservableValue
import pro.piechowski.highschoolstory.inspector.fullTypeName
import pro.piechowski.highschoolstory.inspector.globals.GlobalInstance
import pro.piechowski.highschoolstory.inspector.`object`.ObjectInspectorViewModel
import pro.piechowski.highschoolstory.inspector.`object`.ObjectTableCell

@KoinInternalApi
class GlobalInstancesView(
    viewModel: GlobalInstancesViewModel,
    objectInspectorViewModel: ObjectInspectorViewModel,
) : InspectorView<GlobalInstancesViewModel>(viewModel) {
    private val typeColumn =
        TableColumn<GlobalInstance<Any>, String>().apply {
            minWidth = 100.0
            prefWidth = USE_COMPUTED_SIZE
            maxWidth = Double.MAX_VALUE
            text = "Type"
            cellValueFactory =
                Callback {
                    SimpleStringProperty(it.value.type.fullTypeName)
                }
        }

    private val valueColumn =
        TableColumn<GlobalInstance<Any>, Any?>().apply {
            minWidth = 100.0
            prefWidth = USE_COMPUTED_SIZE
            maxWidth = Double.MAX_VALUE
            text = "Value"
            cellFactory =
                Callback { ObjectTableCell(objectInspectorViewModel) }
            cellValueFactory =
                Callback {
                    SimpleObjectProperty(it.value.value ?: "null")
                }
        }

    private val instancesTable =
        TableView<GlobalInstance<Any>>().apply {
            columnResizePolicy = CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
            maxHeight = Double.MAX_VALUE
            VBox.setVgrow(this, Priority.ALWAYS)

            columns += listOf(typeColumn, valueColumn)

            itemsProperty().bind(viewModel.instances.asObservableValue(coroutineScope))
        }

    private val searchTextField =
        TextField().apply {
            promptText = "Search..."
        }

    override val root =
        VBox().apply {
            prefWidth = 400.0

            children += listOf(searchTextField, instancesTable)
        }
}
