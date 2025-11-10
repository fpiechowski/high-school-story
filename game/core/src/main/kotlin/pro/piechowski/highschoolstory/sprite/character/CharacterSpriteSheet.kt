package pro.piechowski.highschoolstory.sprite.character

import com.badlogic.gdx.graphics.Texture
import pro.piechowski.highschoolstory.animation.character.CharacterAnimation
import pro.piechowski.kge.direction.Direction4
import pro.piechowski.kge.sprite.SpriteSheet

open class CharacterSpriteSheet(
    texture: Texture,
) : SpriteSheet<CharacterSprite>(
        split(
            texture,
            CharacterSprite.sizePixels.x.toInt(),
            CharacterSprite.sizePixels.y.toInt(),
        ) { CharacterSprite(it) },
    ) {
    fun getAnimationFrames(
        kind: CharacterAnimation.Kind,
        direction: Direction4,
    ) = getFrames(kind)
        .getLocomotionFrames(direction)

    private fun List<CharacterSprite>.getLocomotionFrames(direction: Direction4) =
        when (direction) {
            Direction4.Down -> slice(18..23)
            Direction4.Left -> slice(12..17)
            Direction4.Right -> slice(0..5)
            Direction4.Up -> slice(6..11)
        }

    private fun getFrames(animationKind: CharacterAnimation.Kind): List<CharacterSprite> =
        when (animationKind) {
            CharacterAnimation.Kind.IDLE -> sprites[1]
            CharacterAnimation.Kind.WALK -> sprites[2]
        }
}
