package pro.piechowski.highschoolstory.physics.movement.facedirection

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import io.github.oshai.kotlinlogging.KotlinLogging
import pro.piechowski.highschoolstory.debug
import pro.piechowski.highschoolstory.direction.Direction4
import pro.piechowski.highschoolstory.direction.Direction8
import pro.piechowski.highschoolstory.ecs.ReadOnly
import pro.piechowski.highschoolstory.ecs.Write
import pro.piechowski.highschoolstory.physics.movement.input.MovementInput

class FaceDirectionSystem :
    IteratingSystem(
        World.family {
            all(
                @ReadOnly MovementInput.Multiplex,
            ).any(
                @Write FaceDirection4,
                @Write FaceDirection8,
            )
        },
    ) {
    private val logger = KotlinLogging.logger { }

    override fun onTickEntity(entity: Entity) {
        val movementInput = entity[MovementInput.Multiplex]
        val faceDirection = entity[FaceDirection]

        if (movementInput.movementInput != Vector2.Zero) {
            when (faceDirection) {
                is FaceDirection4 -> faceDirection.faceDirection = Direction4.from(movementInput.movementInput)
                is FaceDirection8 -> faceDirection.faceDirection = Direction8.from(movementInput.movementInput)
            }

            logger.debug(faceDirection, entity)
        }
    }
}
