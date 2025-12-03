package pro.piechowski.highschoolstory.vehicle.bus

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import pro.piechowski.highschoolstory.exterior.ExteriorTexture
import pro.piechowski.highschoolstory.physics.body.character.CharacterBody.HEIGHT_TO_DEPTH_RATIO
import pro.piechowski.kge.direction.Direction4
import pro.piechowski.kge.di.DependencyInjection.Global.get
import pro.piechowski.kge.physics.px
import pro.piechowski.kge.sprite.CurrentSprite

sealed class BusSprite(
    textureRegion: TextureRegion,
) : Sprite(textureRegion) {
    init {
        setOrigin(regionWidth.px.toMeter().value / 2, regionHeight.px.toMeter().value / HEIGHT_TO_DEPTH_RATIO)
        setOriginBasedPosition(0f, 0f)
        setSize(regionWidth.px.toMeter().value, regionHeight.px.toMeter().value)
    }

    companion object {
        suspend operator fun invoke(
            color: BusColor,
            direction4: Direction4,
        ) = when (direction4) {
            Direction4.Right ->
                when (color) {
                    // TODO other colors than yellow
                    else -> CurrentSprite(Yellow.Right(get<ExteriorTexture>()))
                }

            Direction4.Down ->
                when (color) {
                    else -> CurrentSprite(Yellow.Down(get<ExteriorTexture>()))
                }

            Direction4.Left ->
                when (color) {
                    else -> CurrentSprite(Yellow.Left(get<ExteriorTexture>()))
                }

            Direction4.Up ->
                when (color) {
                    else -> CurrentSprite(Yellow.Up(get<ExteriorTexture>()))
                }
        }
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
