package pro.piechowski.highschoolstory.ecs

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.EntityTag
import com.github.quillraven.fleks.EntityTags
import com.github.quillraven.fleks.UniqueId

/**
 * Represents an archetype in the Entity Component System (ECS) architecture.
 *
 * <p>An archetype defines a reusable template that specifies a set of components and entity tags
 * that entities should possess. This allows for efficient entity creation by pre-defining common
 * entity configurations.</p>
 *
 * <p>In ECS terminology, an archetype serves as a blueprint that groups together:</p>
 * <ul>
 *   <li>Components - data containers that define entity behavior and properties</li>
 *   <li>Entity Tags - labels for categorizing and filtering entities</li>
 * </ul>
 *
 * <p>Archetypes can be composed together using the {@code +} operator to create more complex
 * entity templates from simpler ones.</p>
 *
 * <h3>Usage Example:</h3>
 * <pre>{@code
 * // Create an archetype with components and tags
 * val playerArchetype = Archetype {
 *     this += PositionComponent(x = 0f, y = 0f)
 *     this += HealthComponent(maxHealth = 100)
 *     this += PlayerTag
 * }
 *
 * // Compose archetypes
 * val armedPlayerArchetype = playerArchetype + weaponArchetype
 *
 * // Apply archetype to an entity
 * world.entity { entity ->
 *     entity += playerArchetype
 * }
 * }</pre>
 *
 * @property components A map of component types to their instances that define this archetype
 * @property entityTags A set of tags that entities of this archetype should possess
 *
 * @see Builder for constructing archetype instances
 * @see Component
 * @see EntityTag
 */
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
        suspend operator fun invoke(builderBlock: suspend Builder.() -> Unit = {}) = Builder().also { it.builderBlock() }.build()
    }
}

context(ecc: EntityCreateContext)
operator fun Entity.plusAssign(archetype: Archetype) =
    with(ecc) {
        this@plusAssign.plusAssign(archetype.entityTags.toList())
        this@plusAssign.plusAssign(archetype.components.values.toList())
    }
