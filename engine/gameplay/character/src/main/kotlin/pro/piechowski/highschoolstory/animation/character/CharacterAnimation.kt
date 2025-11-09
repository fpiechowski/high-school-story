package pro.piechowski.highschoolstory.animation.character

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.collections.GdxArray
import ktx.collections.toGdxArray
import pro.piechowski.highschoolstory.direction.Direction4
import pro.piechowski.highschoolstory.physics.s
import pro.piechowski.highschoolstory.sprite.character.CharacterSprite
import pro.piechowski.highschoolstory.sprite.character.CharacterTextureUtils

sealed class CharacterAnimation(
    keyFrames: GdxArray<CharacterSprite>,
    playMode: PlayMode,
) : Animation<Sprite>(duration.value, keyFrames, playMode) {
    enum class Kind {
        IDLE,
        WALK,
    }

    sealed class Idle(
        keyFrames: GdxArray<CharacterSprite>,
    ) : CharacterAnimation(keyFrames, PlayMode.LOOP) {
        companion object {
            val kind: Kind = Kind.IDLE
        }

        class Up(
            texture: Texture,
        ) : Idle(
                getKeyFrames(
                    texture,
                    kind,
                    Direction4.Up,
                ).toGdxArray(),
            )

        class Down(
            texture: Texture,
        ) : Idle(
                getKeyFrames(
                    texture,
                    kind,
                    Direction4.Down,
                ).toGdxArray(),
            )

        class Left(
            texture: Texture,
        ) : Idle(
                getKeyFrames(
                    texture,
                    kind,
                    Direction4.Left,
                ).toGdxArray(),
            )

        class Right(
            texture: Texture,
        ) : Idle(
                getKeyFrames(
                    texture,
                    kind,
                    Direction4.Right,
                ).toGdxArray(),
            )
    }

    sealed class Walk(
        keyFrames: GdxArray<CharacterSprite>,
    ) : CharacterAnimation(keyFrames, PlayMode.LOOP) {
        companion object {
            val kind: Kind = Kind.WALK
        }

        class Up(
            texture: Texture,
        ) : Walk(
                getKeyFrames(
                    texture,
                    kind,
                    Direction4.Up,
                ).toGdxArray(),
            )

        class Down(
            texture: Texture,
        ) : Walk(
                getKeyFrames(
                    texture,
                    kind,
                    Direction4.Down,
                ).toGdxArray(),
            )

        class Left(
            texture: Texture,
        ) : Walk(
                getKeyFrames(
                    texture,
                    kind,
                    Direction4.Left,
                ).toGdxArray(),
            )

        class Right(
            texture: Texture,
        ) : Walk(
                getKeyFrames(
                    texture,
                    kind,
                    Direction4.Right,
                ).toGdxArray(),
            )
    }

    companion object {
        val duration = (1f / 5f).s

        fun getKeyFrames(
            texture: Texture,
            kind: Kind,
            direction4: Direction4,
        ) = TextureRegion
            .split(
                texture,
                CharacterSprite.Companion.WIDTH.toInt(),
                CharacterSprite.Companion.HEIGHT.toInt(),
            )[CharacterTextureUtils.getAnimationRegionsRow(kind)]
            .slice(CharacterTextureUtils.getLocomotionAnimationRegionsColumnRange(direction4))
            .map { CharacterSprite(it) }
    }
}
