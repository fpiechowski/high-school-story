package pro.piechowski.highschoolstory.inspector.`object`

import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.util.Callback
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.typeOf

class ObjectInspectorView(
    private val initialObject: StateFlow<Any>,
) : TableView<KProperty1<Any, Any?>>() {
    private val logger = KotlinLogging.logger { }

    val propertyColumn =
        TableColumn<KProperty1<Any, Any?>, String>()
            .apply {
                columnResizePolicy = CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
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
                columnResizePolicy = CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
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

    init {
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
}
