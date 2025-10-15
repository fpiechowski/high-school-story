package pro.piechowski.highschoolstory.inspector.ecs

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.World
import org.koin.core.context.GlobalContext
import pro.piechowski.highschoolstory.physics.movement.Speed
import kotlin.collections.flatMap
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class EcsInspectorModel {
    private val world = GlobalContext
        .getOrNull()
        ?.get<World>()

    fun components(type: ComponentType<*>): List<Component<Any>> =
        world
            ?.let { world ->
                val allFamiliesProperty =
                    world::class.memberProperties.find { it.name == "allFamilies" } as? KProperty1<Any, *>
                val families = allFamiliesProperty?.get(world) as Array<*>
                families.map { it as Family }.toTypedArray()
            }?.toList()
            ?.flatMap { family ->
                with(family) {
                    family.entities.mapNotNull { entity ->
                        entity.getOrNull<Any>(type)
                    }
                }
            }
            ?: emptyList()
}
