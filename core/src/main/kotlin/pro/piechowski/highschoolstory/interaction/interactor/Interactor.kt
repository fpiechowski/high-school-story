package pro.piechowski.highschoolstory.interaction.interactor

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import com.github.quillraven.fleks.EntityTag
import com.github.quillraven.fleks.World
import pro.piechowski.highschoolstory.interaction.input.InteractionInput
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection4
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection8
import pro.piechowski.highschoolstory.physics.movement.facedirection.get

data object Interactor : EntityTag()

class InteractorEntity private constructor(
    val entity: Entity,
) {
    context(ecc: EntityComponentContext)
    val position get() = with(ecc) { entity[PhysicsBody].body.position }

    context(ecc: EntityComponentContext)
    val faceDirection: FaceDirection<*> get() = with(ecc) { entity[FaceDirection] }

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
                PhysicsBody,
                InteractionInput,
            ).any(FaceDirection4, FaceDirection8)
        }

val World.Companion.Interactors
    get() =
        family {
            all(
                Interactor,
                PhysicsBody,
                InteractionInput,
            ).any(FaceDirection4, FaceDirection8)
        }
