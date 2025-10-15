package pro.piechowski.highschoolstory.inspector.koin

import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.instance.SingleInstanceFactory
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@KoinInternalApi
class KoinInspector(
    private val gameScope: CoroutineScope,
) : KoinComponent {
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val view = KoinInspectorView()
    private val scene = Scene(view)
    private val controller = KoinInspectorController(view)
    private val stage =
        Stage().apply {
            scene = this@KoinInspector.scene
            title = "Koin"
            x = 0.0
            y = 0.0
        }

    private val instances
        get() =
            GlobalContext
                .getOrNull()
                ?.instanceRegistry
                ?.instances
                ?.values
                ?.filterIsInstance<SingleInstanceFactory<*>>()
                ?.map { factory ->
                    val valueProp = factory::class.memberProperties.find { it.name == "value" }
                    valueProp?.isAccessible = true
                    factory to (valueProp as? KProperty1<Any, *>)?.get(factory)
                }?.sortedBy { it.first.beanDefinition.primaryType.simpleName } ?: emptyList()

    fun show() = stage.show()

    init {
        coroutineScope.launch {
            while (true) {
                controller.updateInstancesTable(instances)
                delay(2000)
            }
        }
    }
}
