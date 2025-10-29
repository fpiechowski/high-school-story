package pro.piechowski.highschoolstory.vehicle.bus

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import pro.piechowski.highschoolstory.character.Character.Companion.HEIGHT_TO_DEPTH_RATIO
import pro.piechowski.highschoolstory.character.rendering.CharacterSprite.Companion.ORIGIN_X
import pro.piechowski.highschoolstory.character.rendering.CharacterSprite.Companion.ORIGIN_Y
import pro.piechowski.highschoolstory.exterior.ExteriorTexture
import pro.piechowski.highschoolstory.physics.PIXELS_PER_METER
import pro.piechowski.highschoolstory.physics.px

sealed class BusSprite(
    textureRegion: TextureRegion,
) : Sprite(textureRegion) {
    init {
        setOrigin(regionWidth.px.toMeter().value / 2, regionHeight.px.toMeter().value / HEIGHT_TO_DEPTH_RATIO)
        setOriginBasedPosition(0f, 0f)
        setSize(regionWidth.px.toMeter().value, regionHeight.px.toMeter().value)
    }

    sealed class Yellow(
        textureRegion: TextureRegion,
    ) : BusSprite(textureRegion) {
        class Left(
            textureRegion: TextureRegion,
        ) : Yellow(textureRegion) {
            companion object {
                suspend operator fun invoke(exteriorTexture: ExteriorTexture): Left =
                    Left(
                        exteriorTexture.region(
                            624,
                            3408,
                            336,
                            192,
                        ),
                    )
            }
        }

        class Right(
            textureRegion: TextureRegion,
        ) : Yellow(textureRegion) {
            companion object {
                suspend operator fun invoke(exteriorTexture: ExteriorTexture): Right =
                    Right(
                        exteriorTexture.region(
                            624,
                            3600,
                            336,
                            192,
                        ),
                    )
            }
        }

        class Down(
            textureRegion: TextureRegion,
        ) : Yellow(textureRegion) {
            companion object {
                suspend operator fun invoke(exteriorTexture: ExteriorTexture): Down =
                    Down(
                        exteriorTexture.region(
                            960,
                            3408,
                            144,
                            336,
                        ),
                    )
            }
        }

        class Up(
            textureRegion: TextureRegion,
        ) : Yellow(textureRegion) {
            companion object {
                suspend operator fun invoke(exteriorTexture: ExteriorTexture): Up =
                    Up(
                        exteriorTexture.region(
                            1104,
                            3408,
                            144,
                            336,
                        ),
                    )
            }
        }
    }
}
