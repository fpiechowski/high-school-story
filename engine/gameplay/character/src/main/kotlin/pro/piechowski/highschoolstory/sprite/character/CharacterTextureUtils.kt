package pro.piechowski.highschoolstory.sprite.character

import pro.piechowski.highschoolstory.animation.character.CharacterAnimation
import pro.piechowski.highschoolstory.direction.Direction4

object CharacterTextureUtils {
    fun getLocomotionAnimationRegionsColumnRange(direction: Direction4) =
        when (direction) {
            Direction4.Down -> 18..23
            Direction4.Left -> 12..17
            Direction4.Right -> 0..5
            Direction4.Up -> 6..11
        }

    fun getAnimationRegionsRow(animationKind: CharacterAnimation.Kind) =
        when (animationKind) {
            CharacterAnimation.Kind.IDLE -> 1
            CharacterAnimation.Kind.WALK -> 2
        }
}
