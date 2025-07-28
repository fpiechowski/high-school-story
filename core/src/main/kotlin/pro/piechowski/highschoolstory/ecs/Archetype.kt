package pro.piechowski.highschoolstory.ecs

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.EntityTag
import com.github.quillraven.fleks.EntityTags
import com.github.quillraven.fleks.UniqueId

class Archetype(
    val components: Map<ComponentType<*>, Component<*>>,
    val entityTags: Set<EntityTags>,
) {
    class Builder(
        val components: MutableMap<ComponentType<*>, Component<*>> = mutableMapOf(),
        val entityTags: MutableSet<EntityTags> = mutableSetOf(),
    ) {
        fun build() = Archetype(components, entityTags)

        inline operator fun <reified T : Component<*>> plusAssign(component: T) {
            components += component.type() to component
        }

        operator fun plusAssign(entityTag: EntityTag) {
            entityTags += entityTag
        }

        operator fun plusAssign(archetype: Archetype) {
            components += archetype.components
            entityTags += archetype.entityTags
        }

        inline operator fun <reified T> get(componentType: ComponentType<T>): T =
            components[componentType].let {
                if (it is T) {
                    it as T
                } else {
                    error("Invalid component type: $it")
                }
            }

        inline operator fun <reified T> get(entityTag: UniqueId<T>): T =
            entityTags.find { it == entityTag }?.let {
                if (it is T) {
                    it as T
                } else {
                    error("Invalid entity tag: $it")
                }
            } ?: error("Entity tag not found: $entityTag")
    }

    operator fun plus(archetype: Archetype) = Archetype(components + archetype.components, entityTags + archetype.entityTags)

    inline operator fun <reified T> get(componentType: ComponentType<T>): T =
        components[componentType].let {
            if (it is T) {
                it as T
            } else {
                error("Invalid component type: $it")
            }
        }

    inline operator fun <reified T> get(entityTag: UniqueId<T>): T =
        entityTags.find { it == entityTag }?.let {
            if (it is T) {
                it as T
            } else {
                error("Invalid entity tag: $it")
            }
        } ?: error("Entity tag not found: $entityTag")

    companion object {
        operator fun invoke(builderBlock: Builder.() -> Unit = {}) = Builder().also { it.builderBlock() }.build()
    }
}

context(ecc: EntityCreateContext)
operator fun Entity.plusAssign(archetype: Archetype) =
    with(ecc) {
        this@plusAssign.plusAssign(archetype.entityTags.toList())
        this@plusAssign.plusAssign(archetype.components.values.toList())
    }
