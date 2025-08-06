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
            textureRegion: TextureRegion,
        ) : Brown(textureRegion) {
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
        ) : Brown(textureRegion) {
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
        ) : Brown(textureRegion) {
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
        ) : Brown(textureRegion) {
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
