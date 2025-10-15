package pro.piechowski.highschoolstory.inspector.koin

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.util.Callback
import org.koin.core.instance.SingleInstanceFactory
import pro.piechowski.highschoolstory.inspector.`object`.ObjectTableCell

class KoinInspectorView : VBox() {
    val typeColumn =
        TableColumn<Pair<SingleInstanceFactory<*>, Any?>, String>().apply {
            minWidth = 100.0
            prefWidth = USE_COMPUTED_SIZE
            maxWidth = Double.MAX_VALUE
            text = "Type"
            cellValueFactory =
                Callback {
                    SimpleStringProperty(it.value.first.beanDefinition.primaryType.simpleName ?: "Unknown")
                }
        }

    val valueColumn =
        TableColumn<Pair<SingleInstanceFactory<*>, Any?>, Any?>().apply {
            minWidth = 100.0
            prefWidth = USE_COMPUTED_SIZE
            maxWidth = Double.MAX_VALUE
            text = "Value"
            cellFactory =
                Callback { ObjectTableCell() }
            cellValueFactory =
                Callback {
                    SimpleObjectProperty(it.value.second ?: "null")
                }
        }

    val instancesTable =
        TableView<Pair<SingleInstanceFactory<*>, Any?>>().apply {
            columnResizePolicy = CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
            maxHeight = Double.MAX_VALUE
            setVgrow(this, Priority.ALWAYS)
            columns += listOf(typeColumn, valueColumn)
        }

    val searchTextField =
        TextField().apply {
            promptText = "Search..."
        }

    init {
        prefWidth = 250.0
        prefHeight = 400.0

        children += listOf(searchTextField, instancesTable)
    }
}
