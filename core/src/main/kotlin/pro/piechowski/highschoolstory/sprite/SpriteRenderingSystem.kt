package pro.piechowski.highschoolstory.sprite

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SpriteRenderingSystem :
    IteratingSystem(family { all(CurrentSprite) }),
    KoinComponent {
    private val spriteBatch: SpriteBatch by inject()

    override fun onTickEntity(entity: Entity) {
        entity[CurrentSprite].sprite.draw(spriteBatch)
    }
}
