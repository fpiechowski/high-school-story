package pro.piechowski.highschoolstory.vehicle.bus

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import pro.piechowski.highschoolstory.exterior.ExteriorTexture

sealed class BusSprite(
    textureRegion: TextureRegion,
) : Sprite(textureRegion) {
    sealed class Brown(
        textureRegion: TextureRegion,
    ) : BusSprite(textureRegion) {
        class Left(
            exteriorTexture: ExteriorTexture,
        ) : Brown(TextureRegion(exteriorTexture.texture, 624, 4176, 659, 4367))
    }
}
