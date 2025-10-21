package pro.piechowski.highschoolstory.inspector.`object`

import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Region.USE_COMPUTED_SIZE
import javafx.stage.Stage
import javafx.stage.WindowEvent
import javafx.util.Callback
import kotlinx.coroutines.flow.StateFlow
import pro.piechowski.highschoolstory.inspector.InspectorView
import pro.piechowski.highschoolstory.inspector.SharedInspectorViewModel
import pro.piechowski.highschoolstory.inspector.`object`.ObjectInspector.Companion.instance
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.typeOf

class ObjectInspectorView(
    override val viewModel: ObjectInspectorViewModel,
) : InspectorView<ObjectInspectorViewModel>() {
    private val logger = KotlinLogging.logger { }

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
                    Callback { ObjectTableCell() }
                cellValueFactory =
                    Callback {
                        SimpleObjectProperty(tryGetPropertyValue(it.value))
                    }
            }
    override val title: String = ""

    override fun Stage.stageSetup() {
        onCloseRequest =
            EventHandler<WindowEvent> {
                viewModel.refreshJob?.cancel()
                if (instance == this@ObjectInspectorView) instance = null
            }
    }

    override val sharedInspectorViewModel: SharedInspectorViewModel
        get() = TODO("Not yet implemented")

    override val root = TableView<KProperty1<Any, Any?>>()
        .apply {
            columnResizePolicy = CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
            columns.addAll(propertyColumn, valueColumn)
        }

    private fun tryGetPropertyValue(property: KProperty1<Any, Any?>): Any? =
        try {
            property.isAccessible = true

            when {
                property.returnType.isSubtypeOf(typeOf<StateFlow<*>>()) -> {
                    (property.get(initialObject.value) as StateFlow<*>).value
                }

                else -> property.get(initialObject.value)
            }
        } catch (e: Throwable) {
            logger.error(e) { "Error while creating cell value for property $property" }
            "error"
        }

    init {
        addEventHandler<MouseEvent>(MouseEvent.MOUSE_CLICKED) { event ->
            when (event.button) {
                MouseButton.BACK -> model.navigateBack()
                MouseButton.FORWARD -> model.navigateForward()
                else -> {}
            }
        }
    }
}
