package pro.piechowski.highschoolstory.interaction

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import ktx.math.minus
import pro.piechowski.highschoolstory.input.interaction.InteractionInput
import pro.piechowski.highschoolstory.interaction.InteractionSystem.Companion.interactionRange
import pro.piechowski.highschoolstory.interaction.interactable.Interactable
import pro.piechowski.highschoolstory.interaction.interactable.InteractableEntity
import pro.piechowski.highschoolstory.interaction.interactable.Interactables
import pro.piechowski.highschoolstory.interaction.interactor.Interactor
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
                    InteractableEntity(it).isInInteractionRangeOf(Interactor(entity))
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
fun InteractableEntity.isInInteractionRangeOf(interactor: Interactor): Boolean =
    with(ecc) {
        val interactablePosition = this@isInInteractionRangeOf.position
        val interactorFaceDirection = interactor.faceDirection

        val fromInteractorToInteractable = (interactablePosition - interactor.position)

        val inRange = fromInteractorToInteractable.len() <= interactionRange.value

        if (inRange) {
            val facing = fromInteractorToInteractable.nor().dot(interactorFaceDirection.faceDirection.nor()) > 0.7f

            return facing
        }

        return false
    }
