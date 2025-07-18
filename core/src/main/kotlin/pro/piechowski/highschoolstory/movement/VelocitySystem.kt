package pro.piechowski.highschoolstory.movement

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import io.github.oshai.kotlinlogging.KotlinLogging
import ktx.math.times
import pro.piechowski.highschoolstory.debug

class VelocitySystem : IteratingSystem(World.family { all(MovementInput.Multiplex, Velocity, Speed) }) {
    private val logger = KotlinLogging.logger { }

    override fun onTickEntity(entity: Entity) {
        val movementInput = entity[MovementInput.Multiplex].movementInput

        val velocity = entity[Velocity]
        velocity.velocity = movementInput * entity[Speed].speed

        if (velocity.velocity != Vector2.Zero.cpy()) {
            logger.debug(velocity, entity)
        }
    }
}
