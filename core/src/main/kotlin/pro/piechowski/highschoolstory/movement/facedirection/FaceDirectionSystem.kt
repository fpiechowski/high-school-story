package pro.piechowski.highschoolstory.movement.facedirection

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import io.github.oshai.kotlinlogging.KotlinLogging
import pro.piechowski.highschoolstory.ReadOnly
import pro.piechowski.highschoolstory.Write
import pro.piechowski.highschoolstory.debug
import pro.piechowski.highschoolstory.direction.Direction8
import pro.piechowski.highschoolstory.movement.input.MovementInput

class FaceDirectionSystem :
    IteratingSystem(
        World.family {
            all(
                @ReadOnly MovementInput.Multiplex,
                @Write FaceDirection,
            )
        },
    ) {
    private val logger = KotlinLogging.logger { }

    override fun onTickEntity(entity: Entity) {
        val movementInput = entity[MovementInput.Multiplex]
        val faceDirection = entity[FaceDirection]

        if (movementInput.movementInput != Vector2.Zero) {
            faceDirection.faceDirection = Direction8.from(movementInput.movementInput)

            logger.debug(faceDirection, entity)
        }
    }
}
