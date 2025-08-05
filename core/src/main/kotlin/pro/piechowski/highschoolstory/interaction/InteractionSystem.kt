package pro.piechowski.highschoolstory.interaction

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import ktx.math.minus
import pro.piechowski.highschoolstory.interaction.InteractionSystem.Companion.interactionRange
import pro.piechowski.highschoolstory.interaction.input.InteractionInput
import pro.piechowski.highschoolstory.interaction.interactable.Interactable
import pro.piechowski.highschoolstory.interaction.interactable.InteractableEntity
import pro.piechowski.highschoolstory.interaction.interactable.Interactables
import pro.piechowski.highschoolstory.interaction.interactor.InteractorEntity
import pro.piechowski.highschoolstory.interaction.interactor.Interactors
import pro.piechowski.highschoolstory.physics.m

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
        val interactionRange = 1.5f.m
    }
}

context(ecc: EntityComponentContext)
fun InteractableEntity.isInInteractionRangeOf(interactorEntity: InteractorEntity): Boolean =
    with(ecc) {
        val interactablePosition = this@isInInteractionRangeOf.position
        val interactorFaceDirection = interactorEntity.faceDirection

        val fromInteractorToInteractable = (interactablePosition - interactorEntity.position)

        val inRange = fromInteractorToInteractable.len() <= interactionRange.value

        if (inRange) {
            val facing = fromInteractorToInteractable.nor().dot(interactorFaceDirection.faceDirection.nor()) > 0.7f

            return facing
        }

        return false
    }
