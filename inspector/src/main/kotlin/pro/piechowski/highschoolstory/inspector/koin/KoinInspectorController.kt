package pro.piechowski.highschoolstory.inspector.koin

import javafx.event.EventHandler
import javafx.scene.control.TableCell
import javafx.scene.control.TableRow
import javafx.util.Callback
import org.koin.core.instance.SingleInstanceFactory
import pro.piechowski.highschoolstory.inspector.`object`.ObjectInspector

class KoinInspectorController(
    private val view: KoinInspectorView,
) {
    fun updateInstancesTable(instances: List<Pair<SingleInstanceFactory<*>, Any?>>) {
        view.instancesTable.items.setAll(instances)
    }
}
