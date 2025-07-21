package pro.piechowski.highschoolstory.sprite

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion

class CharacterSprite(
    textureRegion: TextureRegion,
) : Sprite(textureRegion) {
    companion object {
        const val ORIGIN_X = 24f
        const val ORIGIN_Y = 24f
        const val WIDTH = 48f
        const val HEIGHT = 96f
    }

    init {
        setOrigin(ORIGIN_X, ORIGIN_Y)
        setOriginBasedPosition(0f, 0f)
    }
}
