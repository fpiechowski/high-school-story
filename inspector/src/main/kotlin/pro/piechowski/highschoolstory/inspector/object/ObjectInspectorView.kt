package pro.piechowski.highschoolstory.inspector.`object`

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Region.USE_COMPUTED_SIZE
import javafx.stage.Stage
import javafx.util.Callback
import pro.piechowski.highschoolstory.inspector.InspectorView
import pro.piechowski.highschoolstory.inspector.asObservableValue
import kotlin.reflect.KProperty1

class ObjectInspectorView(
    viewModel: ObjectInspectorViewModel,
) : InspectorView<ObjectInspectorViewModel>(viewModel) {
    val propertyColumn =
        TableColumn<KProperty1<Any, Any?>, String>()
            .apply {
                minWidth = 100.0
                prefWidth = USE_COMPUTED_SIZE
                maxWidth = Double.MAX_VALUE
                text = "Property"
                cellValueFactory =
                    Callback {
                        SimpleStringProperty(it.value.name)
                    }
            }

    val valueColumn =
        TableColumn<KProperty1<Any, Any?>, Any>()
            .apply {
                minWidth = 100.0
                prefWidth = USE_COMPUTED_SIZE
                maxWidth = Double.MAX_VALUE
                text = "Value"
                cellFactory =
                    Callback { ObjectTableCell(viewModel) }
                cellValueFactory =
                    Callback {
                        SimpleObjectProperty(viewModel.tryGetPropertyValue(it.value))
                    }
            }

    override val root =
        TableView<KProperty1<Any, Any?>>()
            .apply {
                prefWidth = 400.0
                columnResizePolicy = CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
                columns.addAll(propertyColumn, valueColumn)

                addEventHandler(MouseEvent.MOUSE_CLICKED) { event ->
                    when (event.button) {
                        MouseButton.BACK -> viewModel.navigateBack()
                        MouseButton.FORWARD -> viewModel.navigateForward()
                        else -> {}
                    }
                }

                itemsProperty().bind(viewModel.properties.asObservableValue(coroutineScope, FXCollections.emptyObservableList()))
            }
}
