package pro.piechowski.highschoolstory.sprite

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.compareEntity
import ktx.graphics.use
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.MeterCamera
import pro.piechowski.highschoolstory.ecs.ReadOnly

class SpriteRenderingSystem :
    IteratingSystem(
        World.family {
            all(
                @ReadOnly CurrentSprite,
            )
        },
        comparator =
            compareEntity { entityA, entityB ->
                val positionA = entityA[CurrentSprite].sprite.y
                val positionB = entityB[CurrentSprite].sprite.y

                positionB.compareTo(positionA)
            },
    ),
    KoinComponent {
    private val spriteBatch: SpriteBatch by inject()
    private val meterCamera: MeterCamera by inject()

    override fun onTickEntity(entity: Entity) {
        spriteBatch.projectionMatrix = meterCamera.combined
        spriteBatch.use {
            entity[CurrentSprite].sprite.draw(spriteBatch)
        }
    }
}
