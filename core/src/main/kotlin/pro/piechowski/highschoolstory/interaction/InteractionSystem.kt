package pro.piechowski.highschoolstory.interaction

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import ktx.math.minus
import pro.piechowski.highschoolstory.interaction.InteractionSystem.Companion.INTERACTION_RANGE
import pro.piechowski.highschoolstory.interaction.input.InteractionInput

class InteractionSystem :
    IteratingSystem(
        World.Interactors,
    ) {
    override fun onTickEntity(entity: Entity) {
        val interactionInput = entity[InteractionInput]

        if (interactionInput.interacting) {
            val interactables =
                world.Interactables.filter {
                    InteractableEntity(it).isInInteractionRangeOf(InteractorEntity(entity))
                }

            interactables.forEach {
                it[Interactable].onInteract()
            }

            interactionInput.interacting = false
        }
    }

    companion object {
        const val INTERACTION_RANGE = 100f
    }
}

context(ecc: EntityComponentContext)
fun InteractableEntity.isInInteractionRangeOf(interactorEntity: InteractorEntity): Boolean =
    with(ecc) {
        val interactablePosition = this@isInInteractionRangeOf.position
        val interactorFaceDirection = interactorEntity.faceDirection

        val fromInteractorToInteractable = (interactablePosition.position - interactorEntity.position.position)

        val inRange = fromInteractorToInteractable.len() <= INTERACTION_RANGE

        if (inRange) {
            val facing = fromInteractorToInteractable.nor().dot(interactorFaceDirection.faceDirection.nor()) > 0.7f

            return facing
        }

        return false
    }
