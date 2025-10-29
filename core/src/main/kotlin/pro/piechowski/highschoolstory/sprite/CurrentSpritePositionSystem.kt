package pro.piechowski.highschoolstory.sprite

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import io.github.oshai.kotlinlogging.KotlinLogging
import pro.piechowski.highschoolstory.debug
import pro.piechowski.highschoolstory.ecs.ReadOnly
import pro.piechowski.highschoolstory.ecs.Write
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.m
import pro.piechowski.highschoolstory.physics.times

class CurrentSpritePositionSystem :
    IteratingSystem(
        World.family {
            all(
                @ReadOnly PhysicsBody,
                @Write CurrentSprite,
            )
        },
    ) {
    private val logger = KotlinLogging.logger { }

    override fun onTickEntity(entity: Entity) {
        val currentSprite = entity[CurrentSprite]
        val position = entity[PhysicsBody].body.position

        currentSprite.sprite.setOriginBasedPosition(position.x, position.y)

        if (Vector2(currentSprite.sprite.x, currentSprite.sprite.y) != position) {
            logger.debug(currentSprite, entity)
        }
    }
}
