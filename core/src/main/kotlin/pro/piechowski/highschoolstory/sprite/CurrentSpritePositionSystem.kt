package pro.piechowski.highschoolstory.sprite

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import io.github.oshai.kotlinlogging.KotlinLogging
import pro.piechowski.highschoolstory.debug
import pro.piechowski.highschoolstory.movement.Position

class CurrentSpritePositionSystem : IteratingSystem(World.family { all(CurrentSprite, Position) }) {
    private val logger = KotlinLogging.logger { }

    override fun onTickEntity(entity: Entity) {
        val currentSprite = entity[CurrentSprite]
        val position = entity[Position]

        currentSprite.sprite.setOriginBasedPosition(position.position.x, position.position.y)

        if (Vector2(currentSprite.sprite.x, currentSprite.sprite.y) != position.position) {
            logger.debug(currentSprite, entity)
        }
    }
}
