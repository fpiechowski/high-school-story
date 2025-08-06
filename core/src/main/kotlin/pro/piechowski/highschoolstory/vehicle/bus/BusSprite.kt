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
                            18144,
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
                suspend operator fun invoke(exteriorTexture: ExteriorTexture): Left =
                    Left(
                        exteriorTexture.region(
                            624,
                            17952,
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
                suspend operator fun invoke(exteriorTexture: ExteriorTexture): Left =
                    Left(
                        exteriorTexture.region(
                            960,
                            18000,
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
                suspend operator fun invoke(exteriorTexture: ExteriorTexture): Left =
                    Left(
                        exteriorTexture.region(
                            1104,
                            18000,
                            144,
                            336,
                        ),
                    )
            }
        }
    }
}
