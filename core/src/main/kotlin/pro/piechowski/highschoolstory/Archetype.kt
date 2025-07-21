package pro.piechowski.highschoolstory

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.EntityTag
import com.github.quillraven.fleks.EntityTags

class Archetype(
    val components: List<Component<*>>,
    val entityTags: List<EntityTags>,
) {
    class Builder(
        private val components: MutableList<Component<*>> = mutableListOf(),
        private val entityTags: MutableList<EntityTags> = mutableListOf(),
    ) {
        fun build() = Archetype(components, entityTags)

        operator fun plusAssign(component: Component<*>) {
            components += component
        }

        operator fun plusAssign(entityTag: EntityTag) {
            entityTags += entityTag
        }

        operator fun plusAssign(archetype: Archetype) {
            components += archetype.components
            entityTags += archetype.entityTags
        }
    }

    operator fun plus(archetype: Archetype) = Archetype(components + archetype.components, entityTags + archetype.entityTags)

    companion object {
        operator fun invoke(builderBlock: Builder.() -> Unit = {}) = Builder().also { it.builderBlock() }.build()
    }
}

context(ecc: EntityCreateContext)
operator fun Entity.plusAssign(archetype: Archetype) =
    with(ecc) {
        this@plusAssign.plusAssign(archetype.entityTags)
        this@plusAssign.plusAssign(archetype.components)
    }
