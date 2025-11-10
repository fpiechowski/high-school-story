package pro.piechowski.highschoolstory.sprite.character

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import pro.piechowski.kge.character.sprite.CharacterSpriteBase
import pro.piechowski.kge.physics.px

class CharacterSprite(
    textureRegion: TextureRegion,
) : CharacterSpriteBase(
        textureRegion,
        ORIGIN_X,
        ORIGIN_Y,
        WIDTH,
        HEIGHT,
    ) {
    companion object {
        const val ORIGIN_X = 24f
        const val ORIGIN_Y = 24f
        private const val WIDTH = 48f
        private const val HEIGHT = 96f
        val sizePixels = Vector2(WIDTH, HEIGHT)
        val size = Vector2(WIDTH.px.toMeter().value, HEIGHT.px.toMeter().value)
    }
}
