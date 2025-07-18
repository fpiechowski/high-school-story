package pro.piechowski.highschoolstory.movement

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import io.github.oshai.kotlinlogging.KotlinLogging
import ktx.math.plus
import pro.piechowski.highschoolstory.debug

class MovementInputSystem :
    IteratingSystem(World.family { any(MovementInput.AI, MovementInput.Controller).all(MovementInput.Multiplex) }) {
    private val logger = KotlinLogging.logger { }

    override fun onTickEntity(entity: Entity) {
        val multiplexMovementInput = entity[MovementInput.Multiplex]
        multiplexMovementInput.movementInput =
            (entity.getOrNull(MovementInput.Controller)?.movementInput ?: Vector2.Zero.cpy()) +
            (entity.getOrNull(MovementInput.AI)?.movementInput ?: Vector2.Zero)

        if (multiplexMovementInput.movementInput != Vector2.Zero.cpy()) {
            logger.debug(multiplexMovementInput, entity)
        }
    }
}
