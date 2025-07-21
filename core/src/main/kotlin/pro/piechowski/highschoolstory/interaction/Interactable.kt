package pro.piechowski.highschoolstory.interaction

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import com.github.quillraven.fleks.World
import pro.piechowski.highschoolstory.movement.position.Position

class Interactable(
    val onInteract: () -> Unit,
) : Component<Interactable> {
    override fun type() = Interactable

    companion object : ComponentType<Interactable>()
}

class InteractableEntity private constructor(
    val entity: Entity,
) {
    context(ecc: EntityComponentContext)
    val position get() = with(ecc) { entity[Position] }

    context(ecc: EntityComponentContext)
    val interactable get() = with(ecc) { entity[Interactable] }

    companion object {
        context(ecc: EntityComponentContext)
        operator fun invoke(entity: Entity) =
            InteractableEntity(entity).apply {
                with(ecc) {
                    requireNotNull(interactable)
                    requireNotNull(position)
                }
            }
    }
}

val World.Interactables get() = family { all(Interactable, Position) }
