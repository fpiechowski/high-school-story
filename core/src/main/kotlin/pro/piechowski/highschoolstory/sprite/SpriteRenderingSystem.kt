package pro.piechowski.highschoolstory.sprite

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ktx.graphics.use
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.ReadOnly

class SpriteRenderingSystem :
    IteratingSystem(
        family {
            all(
                @ReadOnly CurrentSprite,
            )
        },
    ),
    KoinComponent {
    private val spriteBatch: SpriteBatch by inject()

    override fun onTickEntity(entity: Entity) {
        spriteBatch.use {
            entity[CurrentSprite].sprite.draw(spriteBatch)
        }
    }
}
