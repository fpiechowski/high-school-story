package pro.piechowski.highschoolstory.inspector.ecs

import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.World
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class EcsInspector(
    private val gameScope: CoroutineScope,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val view = EcsInspectorView()
    private val scene = Scene(view)
    private val stage =
        Stage().apply {
            scene = this@EcsInspector.scene
            show()
        }

    private val families
        get() =
            GlobalContext
                .getOrNull()
                ?.get<World>()
                ?.let { world ->
                    val allFamiliesProperty =
                        world::class.memberProperties.find { it.name == "allFamilies" } as? KProperty1<Any, *>
                    val families = allFamiliesProperty?.get(world) as Array<*>
                    families.map { it as Family }.toTypedArray()
                }

    init {

        coroutineScope.launch {
            while (true) {
                // TODO(refresh families)
                delay(2000)
            }
        }
    }
}
