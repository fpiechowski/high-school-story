package pro.piechowski.highschoolstory.movement

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import io.github.oshai.kotlinlogging.KotlinLogging
import ktx.math.plusAssign
import ktx.math.times
import pro.piechowski.highschoolstory.debug

class PositionChangeSystem : IteratingSystem(World.family { all(Velocity, Position) }) {
    private val logger = KotlinLogging.logger { }

    override fun onTickEntity(entity: Entity) {
        val position = entity[Position]
        position.position += entity[Velocity].velocity * deltaTime

        logger.debug(position, entity)
    }
}
