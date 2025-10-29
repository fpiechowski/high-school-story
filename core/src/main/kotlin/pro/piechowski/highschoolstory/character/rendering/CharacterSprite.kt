package pro.piechowski.highschoolstory.character.rendering

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import pro.piechowski.highschoolstory.physics.px

class CharacterSprite(
    textureRegion: TextureRegion,
) : Sprite(textureRegion) {
    companion object {
        const val ORIGIN_X = 24f
        const val ORIGIN_Y = 24f
        const val WIDTH = 48f
        const val HEIGHT = 96f
        val size = Vector2(WIDTH.px.toMeter().value, HEIGHT.px.toMeter().value)
    }

    init {
        setOrigin(ORIGIN_X.px.toMeter().value, ORIGIN_Y.px.toMeter().value)
        setOriginBasedPosition(0f, 0f)
        setSize(size.x, size.y)
    }
}
