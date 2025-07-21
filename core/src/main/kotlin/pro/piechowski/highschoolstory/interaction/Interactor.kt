package pro.piechowski.highschoolstory.interaction

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import com.github.quillraven.fleks.EntityTag
import com.github.quillraven.fleks.World
import pro.piechowski.highschoolstory.interaction.input.InteractionInput
import pro.piechowski.highschoolstory.movement.facedirection.FaceDirection
import pro.piechowski.highschoolstory.movement.position.Position

data object Interactor : EntityTag()

class InteractorEntity private constructor(
    val entity: Entity,
) {
    context(ecc: EntityComponentContext)
    val position get() = with(ecc) { entity[Position] }

    context(ecc: EntityComponentContext)
    val faceDirection get() = with(ecc) { entity[FaceDirection] }

    context(ecc: EntityComponentContext)
    val interactionInput get() = with(ecc) { entity[InteractionInput] }

    companion object {
        context(ecc: EntityComponentContext)
        operator fun invoke(entity: Entity) =
            InteractorEntity(entity).apply {
                with(ecc) {
                    entity.has(Interactor)
                    requireNotNull(position)
                    requireNotNull(faceDirection)
                    requireNotNull(interactionInput)
                }
            }
    }
}

val World.Interactors
    get() =
        family {
            all(
                Interactor,
                Position,
                FaceDirection,
                InteractionInput,
            )
        }

val World.Companion.Interactors
    get() =
        family {
            all(
                Interactor,
                Position,
                FaceDirection,
                InteractionInput,
            )
        }
