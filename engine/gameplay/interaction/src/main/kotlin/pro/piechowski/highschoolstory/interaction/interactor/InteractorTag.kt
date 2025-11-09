package pro.piechowski.highschoolstory.interaction.interactor

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityComponentContext
import com.github.quillraven.fleks.EntityTag
import com.github.quillraven.fleks.World
import pro.piechowski.highschoolstory.facedirection.FaceDirection
import pro.piechowski.highschoolstory.facedirection.FaceDirection4
import pro.piechowski.highschoolstory.facedirection.FaceDirection8
import pro.piechowski.highschoolstory.facedirection.get
import pro.piechowski.highschoolstory.input.InputOwner
import pro.piechowski.highschoolstory.input.interaction.InteractionInput
import pro.piechowski.highschoolstory.physics.body.PhysicsBody

data object InteractorTag : EntityTag()

class Interactor private constructor(
    val entity: Entity,
) : InputOwner {
    context(ecc: EntityComponentContext)
    val position get() = with(ecc) { entity[PhysicsBody].body.position }

    context(ecc: EntityComponentContext)
    val faceDirection: FaceDirection<*> get() = with(ecc) { entity[FaceDirection] }

    context(ecc: EntityComponentContext)
    val interactionInput get() = with(ecc) { entity[InteractionInput] }

    companion object {
        context(ecc: EntityComponentContext)
        operator fun invoke(entity: Entity) =
            Interactor(entity).apply {
                with(ecc) {
                    entity.has(InteractorTag)
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
                InteractorTag,
                PhysicsBody,
                InteractionInput,
            ).any(FaceDirection4, FaceDirection8)
        }

val World.Companion.Interactors
    get() =
        family {
            all(
                InteractorTag,
                PhysicsBody,
                InteractionInput,
            ).any(FaceDirection4, FaceDirection8)
        }
