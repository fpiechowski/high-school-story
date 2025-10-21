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
import javafx.stage.Stage
import javafx.util.Callback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.instance.SingleInstanceFactory
import pro.piechowski.highschoolstory.inspector.InspectorView
import pro.piechowski.highschoolstory.inspector.SharedInspectorViewModel
import pro.piechowski.highschoolstory.inspector.asObservableValue
import pro.piechowski.highschoolstory.inspector.`object`.ObjectTableCell

@KoinInternalApi
class KoinInspectorView(
    override val viewModel: KoinInspectorViewModel,
    override val sharedInspectorViewModel: SharedInspectorViewModel
) : InspectorView<KoinInspectorViewModel>() {

    override val title: String = "Koin"

    override fun Stage.stageSetup() {
        x = 0.0
        y = 0.0
    }

    private val typeColumn =
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

    private val valueColumn =
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

    private val instancesTable =
        TableView<Pair<SingleInstanceFactory<*>, Any?>>().apply {
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
            prefWidth = 250.0
            prefHeight = 400.0

            children += listOf(searchTextField, instancesTable)
        }
}
