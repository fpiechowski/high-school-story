package pro.piechowski.highschoolstory.inspector.ecs

import kotlinx.coroutines.flow.Flow
import java.awt.Component

interface ECS {
    val entityComponents: Flow<Map<Entity, List<Component>>>
    val componentTypes: List<ComponentType>

    data class Entity(
        val id: Int,
    )

    data class Component(
        val type: ComponentType,
        val value: Any,
    )

    data class ComponentType(
        val name: String,
    )
}
