package pro.piechowski.highschoolstory.inspector.ecs

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.util.Callback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import pro.piechowski.highschoolstory.inspector.InspectorView
import pro.piechowski.highschoolstory.inspector.asObservableValue
import pro.piechowski.highschoolstory.inspector.`object`.ObjectInspectorViewModel
import pro.piechowski.highschoolstory.inspector.`object`.ObjectTableCell

@ExperimentalCoroutinesApi
class EcsView(
    viewModel: EcsViewModel,
    objectInspectorViewModel: ObjectInspectorViewModel,
) : InspectorView<EcsViewModel>(viewModel) {
    private val entityColumn =
        TableColumn<Pair<ECS.Entity, List<ECS.Component>>, ECS.Entity>()
            .apply {
                text = "Name"
                cellValueFactory =
                    Callback {
                        SimpleObjectProperty(it.value.first)
                    }
            }

    private val componentTypeColumns =
        viewModel.entityComponents
            .map {
                it
                    .flatMap { it.second }
                    .map { it.type }
                    .distinct()
                    .map { componentType ->
                        TableColumn<Pair<ECS.Entity, List<ECS.Component>>, Any?>()
                            .apply {
                                text = componentType.name
                                cellValueFactory =
                                    Callback {
                                        SimpleObjectProperty(
                                            it.value.second
                                                .find { it.type == componentType }
                                                ?.value,
                                        )
                                    }
                                cellFactory =
                                    Callback {
                                        ObjectTableCell(objectInspectorViewModel)
                                    }
                                prefWidth = 100.0
                            }
                    }
            }.runningFold(emptyList<TableColumn<Pair<ECS.Entity, List<ECS.Component>>, Any?>>()) { previousColumns, nextColumns ->
                nextColumns
                    .associateWith { nextColumn -> previousColumns.find { it.text == nextColumn.text } }
                    .mapKeys { (nextColumn, previousColumn) ->
                        previousColumn?.let {
                            nextColumn.apply {
                                prefWidth = previousColumn.width
                            }
                        } ?: nextColumn
                    }.keys
                    .toList()
            }

    private val entityTable =
        TableView<Pair<ECS.Entity, List<ECS.Component>>>().apply {
            componentTypeColumns
                .map { componentTypeColumns ->
                    columns.setAll(listOf(entityColumn) + componentTypeColumns)
                }.launchIn(coroutineScope)

            // columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY_NEXT_COLUMN

            VBox.setVgrow(this, Priority.ALWAYS)

            itemsProperty().bind(
                viewModel.entityComponents.asObservableValue(coroutineScope),
            )
        }

    override val root =
        VBox().apply {
            children += listOf(entityTable)
        }
}
